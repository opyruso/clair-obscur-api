CREATE DATABASE coh_dev CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'coh_dev'@'%' IDENTIFIED BY 'coh_dev_password';
GRANT ALL PRIVILEGES ON coh_dev.* TO 'coh_dev'@'%';
FLUSH PRIVILEGES;

CREATE DATABASE coh_rec CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'coh_rec'@'%' IDENTIFIED BY 'coh_rec_password';
GRANT ALL PRIVILEGES ON coh_rec.* TO 'coh_rec'@'%';
FLUSH PRIVILEGES;

CREATE DATABASE coh_pro CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'coh_pro'@'%' IDENTIFIED BY 'coh_pro_password';
GRANT ALL PRIVILEGES ON coh_pro.* TO 'coh_pro'@'%';
FLUSH PRIVILEGES;
