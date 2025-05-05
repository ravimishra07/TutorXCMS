# utils/loader.py

import json

def load_logs(filepath):
    """
    Loads a .jsonl file where each line is a JSON object.

    Returns:
        A list of dictionaries (parsed entries)
    """
    with open(filepath, 'r', encoding='utf-8') as f:
        return [json.loads(line) for line in f if line.strip()]