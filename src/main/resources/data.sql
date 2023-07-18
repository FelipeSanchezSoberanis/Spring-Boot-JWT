INSERT INTO users (account_non_expired,account_non_locked,credentials_non_expired,enabled,"password",username) VALUES
	 (true,true,true,true,'$argon2id$v=19$m=16384,t=2,p=1$2c2L8Lh5LedbshbM6pWlTA$yWHQ5v2YzjyKsRNMQz+o9Er59h3vTu6JH+UWnzhJNpw','user');
INSERT INTO permissions ("name") VALUES
	 ('READ:users'),
	 ('CREATE:jobs'),
	 ('UPDATE:emojis');
INSERT INTO roles ("name") VALUES
	 ('ROLE_testing');
INSERT INTO permissions_per_role (permissions_id,role_id) VALUES
	 (1,1),
	 (2,1),
	 (3,1);
INSERT INTO roles_per_user (roles_id,user_id) VALUES
	 (1,1);
