from flask import Flask, render_template, request, redirect, url_for, session
from werkzeug.security import generate_password_hash, check_password_hash
import psycopg2
from psycopg2.extras import RealDictCursor
from flask import make_response
from datetime import datetime

app = Flask(__name__)
app.secret_key = 'SECRET_KEY'


def get_db_connection():
    return psycopg2.connect(
        host="localhost",
        database="twitter",
        user="postgres",
        password="123",
        cursor_factory=RealDictCursor
    )

@app.route('/', methods=['GET', 'POST'])
def home():
    if request.method == 'POST':
        action = request.form.get('action')

        username = request.form.get('username')
        password = request.form.get('password')

        if action == 'register':
            hashed_pw = generate_password_hash(password)
            with get_db_connection() as conn:
                with conn.cursor() as cur:
                    cur.execute("SELECT * FROM users WHERE username = %s", (username,))
                if cur.fetchone():
                    return "Username sudah digunakan"
                cur.execute("INSERT INTO users (username, password) VALUES (%s, %s)", (username, hashed_pw))
                conn.commit()
            return redirect(url_for('home'))

        elif action == 'login':
            with get_db_connection() as conn:
                with conn.cursor() as cur:
                    cur.execute("SELECT * FROM users WHERE username = %s", (username,))
                    user = cur.fetchone()
        if user and check_password_hash(user['password'], password):
            session['user_id'] = user['id']
            session['username'] = user['username']
            resp = make_response(redirect(url_for('profile')))
            resp.set_cookie('username', user['username'], max_age=60*60*24)  
            return resp
        else:
            return "Login gagal!"


    return render_template('index.html')
@app.route('/profile')
def profile():
    if 'username' not in session:
        return redirect(url_for('home'))
    return render_template('profil.html', username=session['username'])

@app.route('/post', methods=['POST'])
def post_status():
    content = request.form['content']
    user_id = 2

    conn = get_db_connection()
    cur = conn.cursor()
    cur.execute('''
        INSERT INTO posts (user_id, content, created_at)
        VALUES (%s, %s, %s)
    ''', (user_id, content, datetime.now()))
    conn.commit()
    cur.close()
    conn.close()

    return "OK", 200

@app.route('/load/postingan')
def load_postingan():
    if 'user_id' not in session:
        return '', 401

    conn = get_db_connection()
    cur = conn.cursor()
    cur.execute('''
        SELECT posts.id, content, created_at, username
        FROM posts
        JOIN users ON posts.user_id = users.id
        WHERE users.id = %s
        ORDER BY created_at DESC;
    ''', (session['user_id'],))
    tweets = cur.fetchall()
    conn.close()

    tweet_list = [{
        'id': row['id'],
        'content': row['content'],
        'created_at': row['created_at'],
        'username': row['username'],
    } for row in tweets]

    return render_template('postingan.html', tweets=tweet_list)




@app.route('/load/<section>')
def load_section(section):
    try:
        return render_template(f'{section}.html')
    except:
        return "Section not found", 404

if __name__ == '__main__':
    app.run(debug=True)
