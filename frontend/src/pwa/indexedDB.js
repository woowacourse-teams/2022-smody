import { INDEXED_DB_NAME, INDEXED_DB } from 'constants/storage';

class IndexedDB {
  constructor() {
    this._VERSION = 1.3;
  }

  _openDatabase() {
    return new Promise((resolve, reject) => {
      const request = self.indexedDB.open(INDEXED_DB_NAME, this._VERSION);

      request.onerror = (event) => {
        reject(event);
      };

      request.onupgradeneeded = (event) => {
        const db = event.target.result;
        console.log('IndexedDB 기존 버전:', event.oldVersion);
        console.log('IndexedDB 최신 버전:', this._VERSION);

        db.createObjectStore(INDEXED_DB.FEED, {
          keyPath: 'cycleDetailId',
        });

        db.createObjectStore(INDEXED_DB.CYCLE, {
          keyPath: 'cycleId',
        });

        db.createObjectStore(INDEXED_DB.CHALLENGE, {
          keyPath: 'challengeId',
        });

        db.createObjectStore(INDEXED_DB.RANDOM_CHALLENGE, {
          keyPath: 'challengeId',
        });

        db.createObjectStore(INDEXED_DB.POPULAR_CHALLENGE, {
          keyPath: 'challengeId',
        });
      };

      request.onsuccess = () => {
        resolve(request.result);
      };
    });
  }

  clearPost(name) {
    return new Promise((resolve, reject) => {
      this._openDatabase()
        .then((db) => {
          const transaction = db.transaction(name, 'readwrite');
          const objectStore = transaction.objectStore(name);
          objectStore.clear();

          transaction.oncomplete = (event) => {
            resolve(event);
          };

          transaction.onerror = (event) => {
            reject(event);
          };
        })
        .catch((err) => {
          reject(err);
        });
    });
  }

  savePost(name, data) {
    return new Promise((resolve, reject) => {
      this._openDatabase()
        .then((db) => {
          const transaction = db.transaction(name, 'readwrite');
          const objectStore = transaction.objectStore(name);
          objectStore.add(data);

          transaction.oncomplete = (event) => {
            resolve(event);
          };

          transaction.onerror = (event) => {
            reject(event);
          };
        })
        .catch((err) => {
          reject(err);
        });
    });
  }

  getPosts(name) {
    return new Promise((resolve, reject) => {
      this._openDatabase()
        .then((db) => {
          const posts = [];
          const cursorRequest = db
            .transaction(name)
            .objectStore(name)
            .openCursor(null, 'prev');

          cursorRequest.onsuccess = (event) => {
            const cursor = event.target.result;
            if (cursor) {
              posts.push(cursor.value);
              cursor.continue();
            } else {
              resolve(posts);
            }
          };

          cursorRequest.onerror = (event) => {
            reject(event);
          };
        })
        .catch((err) => {
          reject(err);
        });
    });
  }

  putPost = (name, dataList) => {
    this.clearPost(name).then(() => {
      for (const data of dataList) {
        this.savePost(name, data);
      }
    });
  };
}

export const indexedDB = new IndexedDB();

export const saveDataToCache = (name, pageLength, data) => {
  if (pageLength !== 1) {
    return;
  }

  indexedDB.putPost(name, data);
};
