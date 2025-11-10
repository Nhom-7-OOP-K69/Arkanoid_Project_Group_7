# Arkanoid_Project_Group_7

biểu đồ UML: https://drive.google.com/file/d/1-cefNPk5j6avyExFGgoSmn_D-vCqloye/view?usp=sharing


🎮 Giới thiệu chung
Dungeon Breaker là một phiên bản lấy cảm hứng từ trò chơi arcade kinh điển Arkanoid,
nhưng được khoác lên một lớp áo ngục tối (dungeon) bí ẩn và rực lửa.

Thay vì phi thuyền và gạch neon, người chơi sẽ phải phá hủy các khối đá ma thuật
trong một hầm ngục u tối được bao trùm bởi ánh sáng đỏ rực.

Đây là sự kết hợp hoàn hảo giữa lối chơi phá gạch cổ điển và không khí phiêu lưu rùng rợn.
🕹️ Lối chơi & Điều khiển

Người chơi điều khiển Vaus (mái chèo) — được tùy biến thành một bệ đá hầm ngục ở cuối màn hình.

🎯 Mục tiêu chính:

Phá gạch: Sử dụng quả bóng ma thuật để phá hủy toàn bộ các khối đá/gạch ma thuật trên màn hình.

Sống sót: Không để quả bóng rơi khỏi cạnh dưới của khu vực chơi.

Thu thập: Nhặt các Power-Up rơi ra để tăng cường sức mạnh
🎨 Tài nguyên & Nguồn tham khảo

Chúng tôi đã sử dụng các tài nguyên chất lượng cao để xây dựng môi trường game:

Đồ họa & Thiết kế:
Hầu hết các tài sản đồ họa (Sprites) của khối gạch, bệ đỡ và phông nền được tùy biến dựa trên tài nguyên từ Freepik.
🔗 Link tham khảo: https://www.freepik.com/

Âm thanh & Nhạc nền:
Để tạo ra không khí hầm ngục, chúng tôi sử dụng nhạc nền và hiệu ứng từ các thư viện miễn phí bản quyền.
🔗 Link tham khảo (ví dụ): https://www.zapsplat.com/

⚙️ Cấu trúc Mã nguồn
Dự án được tổ chức thành các gói logic:

1. manager (Quản lý Tài nguyên & Input)
Chịu trách nhiệm tải và quản lý tài nguyên (ảnh, âm thanh, font) và xử lý đầu vào người dùng.

GameManager.java: Lớp điều khiển cốt lõi, chứa vòng lặp game chính.

ImgManager.java: Tải và quản lý hình ảnh song song.

AudioManager.java: Quản lý âm lượng và phát nhạc/sfx.

UIManager.java: Xây dựng và quản lý giao diện (Menu, Settings, Score display).

InputHandler.java: Map phím bấm thành hành động di chuyển và phóng bóng.

2. game (Cài đặt & Trạng thái)
GameConstants.java: Tập hợp các hằng số game (kích thước, tốc độ, HP, v.v.).

GameStateManager.java: Theo dõi trạng thái hiện tại của game (MENU, PLAYING, PAUSED...).

Lives.java: Quản lý mạng sống của người chơi.

LevelIntro.java, GameOverScreen.java, GameWinScreen.java: Các lớp giao diện cho các màn hình trạng thái game.

3. object (Các Thực thể Game)
Bao gồm các đối tượng tương tác trong game:

Gạch (brick): NormalBrick, StrongBrick, SuperBrick, ExplosionBrick, Wall.

Thanh đỡ (paddle): Paddle.java xử lý di chuyển và animation mở rộng mượt mà (updateAnimation).

Bóng (ball): Ball.java xử lý va chạm phức tạp (góc nảy phụ thuộc vào vị trí va chạm trên thanh đỡ).

Vụ nổ (explosion): Explosion.java tạo hiệu ứng hạt (particles) khi gạch bị phá.

4. powerUp (Vật phẩm)
Quản lý các vật phẩm hỗ trợ:

PowerUpManager.java: Xử lý việc rơi vật phẩm, kiểm tra va chạm với thanh đỡ, và quản lý hiệu lực/thời gian của các power-up đang hoạt động.

ExpandPaddlePowerUp.java: Kích hoạt hiệu ứng mở rộng thanh đỡ.

ExtraBallPowerUp.java: Thêm 2 quả bóng mới vào trò chơi.

BulletPowerUp.java: Cho phép thanh đỡ bắn đạn theo chu kỳ.

5. score (Điểm số & Xếp hạng)
Score.java: Theo dõi điểm số hiện tại.

Ranking.java: Xử lý việc tải/lưu bảng xếp hạng (Top 5) vào tệp ranking.txt.

💡 Các tính năng nổi bật
Chuyển đổi Trạng thái Game (Menu, Playing, Paused, Win, Lose).

Hệ thống Power-up đa dạng (Thêm bóng, Mở rộng thanh đỡ, Bắn đạn) với logic quản lý thời gian hiệu lực và hiệu ứng animation.

Va chạm vật lý nâng cao với thanh đỡ (góc nảy phụ thuộc vào điểm va chạm).

Hiệu ứng Vụ nổ dựa trên hệ thống hạt (particle system) khi gạch bị phá.

Tải Level từ tệp text bên ngoài (data/Level_X.txt).

Lưu/Tải Bảng xếp hạng liên tục (ranking persistence).
