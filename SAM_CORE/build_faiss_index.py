import os, json
from sentence_transformers import SentenceTransformer
import faiss
import numpy as np
from glob import glob

model = SentenceTransformer("all-MiniLM-L6-v2")

logs_path = "sam_logs_output"
index = faiss.IndexFlatL2(384)  # 384 = embedding dim for MiniLM

all_logs = []
all_embeddings = []

for file in sorted(glob(os.path.join(logs_path, "*.jsonl"))):
    with open(file, "r", encoding="utf-8") as f:
        for line in f:
            log = json.loads(line)
            text = log["text"]
            embedding = model.encode(text)
            all_logs.append(log)
            all_embeddings.append(embedding)

vectors = np.array(all_embeddings).astype("float32")
index.add(vectors)

faiss.write_index(index, "sam_index.faiss")

with open("sam_index_logs.json", "w", encoding="utf-8") as f:
    json.dump(all_logs, f, ensure_ascii=False, indent=2)

print(f"✅ Index built. {len(all_logs)} logs stored.")
