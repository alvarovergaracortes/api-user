-- las password se crearon con la clase: cl.bci.common.helper.CreateEncryptedPassword.java
-- Usuario con rol ADMIN
-- password: Admin123  --->  $2a$10$wsBmAjqtSTYEmaObHOmb8OSrX0YEldBNThpc/EJYyawSclzyuJdfy
INSERT INTO users (
    id, name, email, password, created, modified, last_login, token, is_active, roles
) VALUES (
    '11111111-1111-1111-1111-111111111111',
    'Administrador',
    'admin@example.com',
    '$2a$10$wsBmAjqtSTYEmaObHOmb8OSrX0YEldBNThpc/EJYyawSclzyuJdfy',
    NOW(),
    NOW(),
    NOW(),
    'token_admin_abc123',
    TRUE,
    'ADMIN'
);

-- Usuario con rol USER
-- password: User123  --->  $2a$10$lyhApG7xdG8QBEi5AgTY5e4kKh7zA0/2GLGuAT3Ymsyn3p.rvQJmG
INSERT INTO users (
    id, name, email, password, created, modified, last_login, token, is_active, roles
) VALUES (
    '22222222-2222-2222-2222-222222222222',
    'Usuario',
    'user@example.com',
    '$2a$10$lyhApG7xdG8QBEi5AgTY5e4kKh7zA0/2GLGuAT3Ymsyn3p.rvQJmG',
    NOW(),
    NOW(),
    NOW(),
    'token_user_xyz789',
    TRUE,
    'USER'
);
