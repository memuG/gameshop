INSERT INTO game (title, price, version) VALUES
  ('Fallout', 1.99, 0),
  ('Don''t Starve', 2.99, 0),
  ('Baldur''s Gate', 3.99, 0),
  ('Icewind Dale', 4.99, 0),
  ('Bloodborne', 5.99, 0);

INSERT INTO cart (id) VALUES 1;
INSERT INTO cart (id) VALUES 2;

INSERT INTO carts_games (cart_id, game_id, game_count) VALUES (1, 1, 3);