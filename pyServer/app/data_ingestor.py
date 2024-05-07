"""
This module is used for data fetching by the ThreadPool individual threads
"""
import pandas as pd

class DataIngestor:
    """
    Class used for data fetching by the ThreadPool individual threads
    """
    def __init__(self, csv_path: str):
        self.data = pd.read_csv(csv_path)
