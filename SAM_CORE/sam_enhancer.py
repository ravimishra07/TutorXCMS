import os
import json
import torch
import argparse
from tqdm import tqdm
from datetime import datetime
from sentence_transformers import SentenceTransformer, util

# ----------------------------
# CLI ARGUMENTS
# ----------------------------

parser = argparse.ArgumentParser(description="Enhance SAM logs with embeddings and mood tagging.")
parser.add_argument("--file", type=str, help="Path to specific JSONL file.")
parser.add_argument("--debug", action="store_true", help="Enable debug logging.")
args = parser.parse_args()

# ----------------------------
# SETUP
# ----------------------------

# Force CPU for stability (MPS can hang)
device = "cpu"
print(f"Device set to use {device}")

model = SentenceTransformer("paraphrase-MiniLM-L3-v2", device=device)

input_folder = "logs"
output_folder = "logs_enhanced"
os.makedirs(output_folder, exist_ok=True)

# ----------------------------
# HELPER FUNCTIONS
# ----------------------------

def load_jsonl(filepath):
    with open(filepath, "r", encoding="utf-8") as f:
        return [json.loads(line.strip()) for line in f]

def save_jsonl(filepath, data):
    with open(filepath, "w", encoding="utf-8") as f:
        for item in data:
            f.write(json.dumps(item, ensure_ascii=False) + "\n")

def generate_embedding(text):
    try:
        emb = model.encode(text, convert_to_numpy=True)
        return emb.tolist()
    except Exception as e:
        print(f"[ERROR] Embedding failed: {e}")
        return None

def enhance_entry(entry):
    content = entry.get("content", "")
    if not content:
        return entry

    entry["embedding"] = generate_embedding(content)
    entry["enhanced_at"] = datetime.now().isoformat()
    return entry

def process_file(filepath, debug=False):
    data = load_jsonl(filepath)
    if debug:
        print(f"[DEBUG] Loaded {len(data)} entries from {filepath}")

    enhanced_data = []
    for entry in tqdm(data, desc="Enhancing logs"):
        enhanced = enhance_entry(entry)
        enhanced_data.append(enhanced)

    filename = os.path.basename(filepath)
    output_path = os.path.join(output_folder, filename)
    save_jsonl(output_path, enhanced_data)

    print(f"[✔] Saved enhanced logs to {output_path}")

# ----------------------------
# MAIN
# ----------------------------

if args.file:
    process_file(args.file, debug=args.debug)
else:
    files = [f for f in os.listdir(input_folder) if f.endswith(".jsonl")]
    for f in files:
        process_file(os.path.join(input_folder, f), debug=args.debug)