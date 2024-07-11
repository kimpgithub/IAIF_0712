import os
import logging
from datetime import datetime
from flask import Flask, request, jsonify, render_template

app = Flask(__name__)

# 로깅 설정
logging.basicConfig(level=logging.INFO)

# 업로드된 파일을 저장할 디렉토리 설정
UPLOAD_FOLDER = 'uploads'
if not os.path.exists(UPLOAD_FOLDER):
    os.makedirs(UPLOAD_FOLDER)
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER

@app.route('/')
def index():
    return render_template('upload.html')

@app.route('/upload', methods=['POST'])
def upload_file():
    if 'file' not in request.files:
        return jsonify({"error": "No file part"}), 400

    file = request.files['file']

    if file.filename == '':
        return jsonify({"error": "No selected file"}), 400

    if file:
        # 현재 날짜와 시간을 기반으로 파일명 생성
        timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")
        file_ext = os.path.splitext(file.filename)[1]  # 파일 확장자 추출
        new_filename = f"audio_{timestamp}{file_ext}"
        file_path = os.path.join(app.config['UPLOAD_FOLDER'], new_filename)
        file.save(file_path)

        # 파일 정보 로그 출력
        app.logger.info(f"File saved: {file_path}")
        app.logger.info(f"File size: {os.path.getsize(file_path)} bytes")

        return jsonify({"message": "File successfully saved", "file_path": file_path})

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0', port=5000)
