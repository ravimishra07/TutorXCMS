import os
import json
import argparse
import faiss
import numpy as np
from tqdm import tqdm
from sentence_transformers import SentenceTransformer

LOGS_DIR = "../sam_logs_cleaned"
INDEX_PATH = "sam_index.faiss"
INDEX_LOGS_PATH = "sam_index_logs.json"
MODEL_NAME = "sentence-transformers/paraphrase-MiniLM-L3-v2"

model = SentenceTransformer(MODEL_NAME)

def build_index():
    print("🔧 Building FAISS index from logs...")
    all_logs = []
    texts = []

    for filename in os.listdir(LOGS_DIR):
        if not filename.endswith(".jsonl"):
            continue
        # Only index recent files
        if "2025-03" not in filename and "2025-04" not in filename:
            continue

        filepath = os.path.join(LOGS_DIR, filename)
        print(f"🔍 Scanning {filename}...")

        with open(filepath, "r", encoding="utf-8") as f:
            for line in f:
                try:
                    entry = json.loads(line)
                    text = entry.get("text", "").strip()
                    if not text or text.startswith("```"):
                        continue
                    role = entry.get("role", "").lower().strip()
                    if "user" in role or role in ["", "-"]:
                        texts.append(text)
                        all_logs.append(entry)
                except Exception as e:
                    print(f"❌ Error reading log line: {e}")
                    continue

    if not texts:
        print("❌ No valid user logs found. Exiting.")
        exit()

    print(f"📦 Found {len(texts)} logs to index.")
    embeddings = model.encode(
        texts,
        convert_to_numpy=True,
        show_progress_bar=True,
        batch_size=32
    ).astype("float32")

    index = faiss.IndexFlatL2(embeddings.shape[1])
    index.add(embeddings)

    faiss.write_index(index, INDEX_PATH)
    with open(INDEX_LOGS_PATH, "w", encoding="utf-8") as f:
        json.dump(all_logs, f, ensure_ascii=False)

    print(f"✅ Indexed {len(all_logs)} entries.\n")
    return index, all_logs

def load_index():
    if not os.path.exists(INDEX_PATH) or not os.path.exists(INDEX_LOGS_PATH):
        return build_index()
    index = faiss.read_index(INDEX_PATH)
    with open(INDEX_LOGS_PATH, "r", encoding="utf-8") as f:
        logs = json.load(f)
    return index, logs

def semantic_search(query, top_k=5):
    index, logs = load_index()
    q_embedding = model.encode([query]).astype("float32")
    D, I = index.search(q_embedding, top_k)

    print(f"\n🔍 Query: {query}\n")
    for rank, idx in enumerate(I[0]):
        log = logs[idx]
        mood = log.get("mood", "unknown").upper()
        date = log.get("date", "no-date")
        text = log.get("text", "")[:500]

        print(f"#{rank+1} | {date} | [{mood}] {log.get('emotion', '-')}")
        print(text)
        print("-" * 80)

if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("--query", help="Search query string")
    parser.add_argument("--top_k", type=int, default=5, help="Number of results to return")
    args = parser.parse_args()

    if args.query:
        semantic_search(args.query, args.top_k)
    else:
        while True:
            query = input("\n🧠 Enter search query (or 'exit'): ").strip()
            if query.lower() == "exit":
                break
            semantic_search(query)