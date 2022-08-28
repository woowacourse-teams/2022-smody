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
      };

      request.onsuccess = () => {
        resolve(request.result);
      };
    });
  }

  clearFeed() {
    return new Promise((resolve, reject) => {
      this._openDatabase()
        .then((db) => {
          const transaction = db.transaction('feed', 'readwrite');
          const feedObjectStore = transaction.objectStore('feed');
          feedObjectStore.clear();

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

  saveFeed(data) {
    return new Promise((resolve, reject) => {
      this._openDatabase()
        .then((db) => {
          const transaction = db.transaction('feed', 'readwrite');
          const feedObjectStore = transaction.objectStore('feed');
          feedObjectStore.add(data);

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

  getFeeds() {
    return new Promise((resolve, reject) => {
      this._openDatabase()
        .then((db) => {
          const feeds = [];
          const cursorRequest = db
            .transaction('feed')
            .objectStore('feed')
            .openCursor(null, 'prev');

          cursorRequest.onsuccess = (event) => {
            const cursor = event.target.result;
            if (cursor) {
              feeds.push(cursor.value);
              cursor.continue();
            } else {
              resolve(feeds);
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
