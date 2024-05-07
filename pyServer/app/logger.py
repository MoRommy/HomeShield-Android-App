import time
import logging
from logging.handlers import RotatingFileHandler


class MyLogger:
    def __init__(self):
        logger = logging.getLogger(__name__)
        handler = RotatingFileHandler("serverEvents.log", maxBytes=100000, backupCount=5)
        formatter = logging.Formatter('%(asctime)s - %(levelname)s - %(message)s', datefmt='%Y-%m-%d %H:%M:%S')
        formatter.converter = time.gmtime
        handler.setFormatter(formatter)
        logger.addHandler(handler)
        logger.setLevel(logging.INFO)
        self.logger = logger
        self.log = True

    def info(self, message):
        if self.log:
            self.logger.info(message)

    def error(self, message):
        if self.log:
            self.logger.error(message)