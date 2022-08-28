class IndexedDB {
  constructor() {
    this._VERSION = 1;
  }

  _openDatabase() {
    return new Promise((resolve, reject) => {
      const request = self.indexedDB.open('smody-db', this._VERSION);

      request.onerror = (event) => {
        reject(event);
      };

      request.onupgradeneeded = (event) => {
        const db = event.target.result;
        console.log('IndexedDB 기존 버전:', event.oldVersion);
        console.log('IndexedDB 최신 버전:', this._VERSION);

        db.createObjectStore('feed', {
          keyPath: 'cycleDetailId',
        });

        db.createObjectStore('cycle', {
          keyPath: 'cycleId',
        });

        db.createObjectStore('challenge', {
          keyPath: 'challengeId',
        });

        db.createObjectStore('myChallenge', {
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
          console.log('db data', data);

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
}

export const indexedDB = new IndexedDB();
