import os
import multiprocessing
from queue import Queue
from threading import Thread, Event

class ThreadPool:
    def __init__(self, ingestor, logger):
        self.num_threads = int(os.getenv("TP_NUM_OF_THREADS", multiprocessing.cpu_count()))
        self.task_queue = Queue()
        self.threads = []
        self.responses = [None] * 1000
        self.ingestor = ingestor
        self.logger = logger

    def start(self):
        for i in range(self.num_threads):
            thread = TaskRunner(i, self.task_queue, self.responses, self.ingestor, self.logger)
            self.threads.append(thread)
            thread.start()

    def add_task(self, path, json, job_id):
        self.task_queue.put((path, json, job_id))

    def gracefulShutDown(self):
        for thread in self.threads:
            thread.stop()


class TaskRunner(Thread):
    def __init__(self, id, task_queue, responses, ingestor, logger):
        Thread.__init__(self)
        self.stop_event = Event()
        self.task_queue: Queue = task_queue
        self.id = id
        self.responses = responses
        self.ingestor = ingestor
        self.logger = logger

    def run(self):
        while not self.stop_event.is_set():
            try:
                self.logger.info(f"[{self.id}]: Waiting for a new job...")
                (path, json, job_id) = self.task_queue.get(block = True)
                self.responses[job_id] = None
                self.responses[job_id] = self.execute(path, json, job_id)
            except Exception as e:
                self.logger.error(f"[{self.id}]: Execution of {job_id} exited with exception: {e}")

    def stop(self):
        self.stop_event.set()

    def execute(self, path, json, job_id):
        self.logger.info(f"[{self.id}]: Started execution of job_id {job_id}.")
        data = self.ingestor.data
        request_data = data[data['Question'] == json['question']]

        match(path):
            case "/api/get_users":
                result = self.execute_get_users(request_data)
            case "/api/get_devices":
                result = self.execute_get_devices(request_data)
        self.logger.info(f"[{self.id}]: Execution of {job_id} is done!")
        return result

    def execute_get_users(self, data):
        return None

    def execute_get_devices(self, data):
        return None

