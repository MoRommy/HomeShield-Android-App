from flask import request, jsonify
from app import webserver

@webserver.route('/api/get_results/<job_id>', methods=['GET'])
def get_response(job_id):
    """
    This function is responsible of fetching the finalized jobs from
    the ThreadPool

    Returns:
    A JSON with the current status and also the processed data (in case of done)
    """
    webserver.logger.info(f"[ROUTE]: Called with path: {request.path}")
    job_id = int(job_id)
    if job_id < 1 or job_id > webserver.job_counter:
        result = jsonify({'status': 'Unknown job_id'})

    elif webserver.tasks_runner.responses[job_id] is not None:
        data = webserver.tasks_runner.responses[job_id]
        result = jsonify({
            'status': 'done',
            'data': data
        })
    else:
        result = jsonify({'status': 'running'})

    webserver.logger.info(f"[ROUTE]: Exit job {job_id}")
    return result

@webserver.route('/api/get_users', methods=['POST'])
def state_mean_request():
    """
    Call the responsable function for processing the requests
    """
    return process_post_request()

@webserver.route('/api/get_devices', methods=['POST'])
def state_mean_request():
    """
    Call the responsable function for processing the requests
    """
    return process_post_request()

def process_post_request():
    """
    Main responsability of this function is to get a new job_id, and add the
    submitted task in ThreadPool queue in order to be executed by a free thread

    Input data:
    The JSON contains the request data

    Returns:
    In case of valid input data, it returns a JSON with the associated job_id
    for the desired task.
    Otherwise, an error message.
    """
    if request.method == 'POST':
        webserver.logger.info(f"[ROUTE]: Called with path: {request.path}")
        job_id = webserver.getNewJobId()
        webserver.tasks_runner.add_task(request.path, request.get_json(), job_id)

        result = jsonify({
            "job_id": str(job_id)
            })
    else:
        result = jsonify({"error": "Method not allowed"}), 405

    webserver.logger.info(f"[ROUTE]: Exit with: {result}")
    return result

@webserver.route('/api/graceful_shutdown')
def force_stop():
    """
    This function is responsable with a warm stop of the application
    The threads cannot take another job, but can finalize the started ones.

    Returns:
    A JSON with the application status
    """
    webserver.logger.info(f"[ROUTE]: Called with path: {request.path}")
    webserver.tasks_runner.gracefulShutDown()
    result = jsonify({
        "status": "powered off"
    })
    webserver.logger.info(f"[ROUTE]: Exit with: {result}")
    return result
