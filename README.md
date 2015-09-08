# SimpleDBProject
Android library to simplify the work with databases. 
## Important!

This library should be used in Application context. Whe have to create singleton controller class. Results from the callbacks should be 
passed to activty/ fragments by EventBus. 
## SDK Support

From SDK API 11


## Gradle
``` java
 maven {
        url 'https://dl.bintray.com/gunslingerpl/maven'
    }
    
 dependencies {
         compile 'pl.com.adammistal:simpledb:0.1.0@aar'
    }

```
## Features

* Add rows
* Remove rows
* Update rows
* Selecting rows
* Request cancelation

## Initialization
``` java
  // we have to init in application context
        SimplyDB.init(this, DB_NAME, DB_VERSION, new ISQLiteOpenHelper() {
            @Override
            public void onCreate(SQLiteDatabase db) {

                String table = QueryUtil.createTable(UserTable.NAME)
                        .addColumn(UserTable.COL_NAME,"TEXT")
                        .addColumn(UserTable.COL_NATIONALITY,"TEXT")
                        .addColumn(UserTable.COL_AGE,"INTEGER").build();

                db.execSQL(table);

            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                db.execSQL("DROP TABLE IF EXISTS "+UserTable.NAME);
            }
        });

```

## How to use

* Insert row to table

``` java
 ContentValues cv = new ContentValues();
        cv.put(UserTable.COL_NAME,user.name);
        cv.put(UserTable.COL_NATIONALITY,user.nationality);
        cv.put(UserTable.COL_AGE,user.age);
        SimplyDB.getInstance().insert(cv).toTable(UserTable.NAME).execute(new IInsertCallback() {
            @Override
            public void onInsertFinished(long rowId) {
                //this will be executed on main thread
            }

            @Override
            public void onError(Object obj) {
                //this will be executed on main thread

            }
        },"addUserTAG");


```

* Remove row from table

``` java
 SimplyDB.getInstance().deleteFrom(UserTable.NAME).rowsWhere(UserTable.COL_NAME+" = "+name).execute(new IDeleteCallback() {
            @Override
            public void onDeleteFinished(int rowsDeleted) {

            }

            @Override
            public void onError(Object obj) {

            }
        },"removeUserTAG"+name);

```

* Update row in db

``` java

  ContentValues cv = new ContentValues();
        cv.put(UserTable.COL_NAME,"TestName");
        cv.put(UserTable.COL_NATIONALITY,"testNationality");
        cv.put(UserTable.COL_AGE,30);

        SimplyDB.getInstance().update(UserTable.NAME).where(UserTable.COL_NAME + " = " + name).whith(cv).execute(new IUpdateCallback() {
            @Override
            public void onUpdateFinished(int rowsAffected) {
                
            }

            @Override
            public void onError(Object obj) {

            }
        },"updateUserTAG"+name);

```

* Selecting rows

``` java

        SimplyDB.getInstance().select("*").fromTable(UserTable.NAME).execute(new ICursorCallback() {
            @Override
            public Object onParseCursor(Cursor cursor) {
                //this will be executed on background , in asynck task
                ArrayList<User> users = new ArrayList<>();
                while (cursor.moveToNext()) {
                    String userName = cursor.getString(1);
                    String nationality = cursor.getString(2);
                    int age = cursor.getInt(3);

                    User user = new User();
                    user.age=age;
                    user.name=userName;
                    user.nationality=nationality;
                    users.add(user);

                }

                return users;
            }

            @Override
            public void onExecuted(Object object) {
                //this will be executed on main thread

            }

            @Override
            public void onCanceled() {
                //this will be executed on main thread

            }

            @Override
            public void onError(Throwable object) {
                //this will be executed on main thread

            }
        },"showAllUsers");


```

* Cancel request

``` java
   SimplyDB.getInstance().cancelExecution(TAG);
```
   
## License

    Copyright 2015 Adam Miśtal

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
