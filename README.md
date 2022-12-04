# DiceTrip

## FastAPI Backend Server Setting

1. Install `MySQL` and `Python 3`.
2. Install FastAPI

```
pip install "fastapi[all]"
```

4. Create MySQL Database named `3330group`.

5. Register a new user by sending a POST request to `/auth/register`. You can use the API using Swagger API. Enter `http://localhost:8000/docs#/auth/register_auth_register_post` in your browser. Then, click _Try it out_. Finally, fill out the email and password. When you log-in later, use this information.

6. Create `.env` file in the project's root directory, and fill up the information. Reference `.env_form.txt` for details.

```
.env          <--- Create this file
.env_form.txt
.git
.gitignore
...
```

```
MYSQL_HOST=localhost
MYSQL_DB_NAME=3330group
MYSQL_USERNAME={username}
MYSQL_PASSWORD={password}

SECRET_KEY=bc56b064e7da028325abeb9eb343a7a3142e972492a707d1a842119a0b08d76a
ALGORITHM=HS256
ACCESS_TOKEN_EXPIRE_MINUTES=1
```

4. Install required Python packages on root directory.

```
pip install -r requirements.txt
```

5. Run the server

```
uvicorn backend.main:app --reload
```

6. Run the frontend and test!
