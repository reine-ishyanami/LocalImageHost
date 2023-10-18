CREATE TABLE IF NOT EXISTS "imgUrl"
(
    "id"      INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "path"    TEXT    NOT NULL,
    "project" TEXT    NOT NULL,
    "name"    TEXT    NOT NULL
);

CREATE UNIQUE INDEX IF NOT EXISTS "imgPath"
ON "imgUrl" (
     "name" ASC,
     "project"
);