from flask import Flask
from app.task_runner import ThreadPool
from app.data_ingestor import DataIngestor
from app.logger import MyLogger

webserver = Flask(__name__)

ingestor = DataIngestor("./database.csv")
logger = MyLogger()

webserver.tasks_runner = ThreadPool(ingestor, logger)
webserver.tasks_runner.start()
webserver.logger = logger
webserver.job_counter = 1
webserver.job_status = {}

def get_new_job_id():
    """
    Increments the job_id counter after getting the current one
    """
    job_id = webserver.job_counter
    webserver.job_counter += 1
    return job_id

webserver.getNewJobId = get_new_job_id

import app.routes