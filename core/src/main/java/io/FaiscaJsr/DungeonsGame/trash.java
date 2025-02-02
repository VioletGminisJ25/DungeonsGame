package io.FaiscaJsr.DungeonsGame;

// Collections.sort(rooms, new RoomComparator());
// for (int i = 0; i < rooms.size(); i++) {
// Room room2 = rooms.get(i);
// Vector2 prevCenter = rooms.get(i).center;

// if (i != 0) {
// prevCenter = rooms.get(i - 1).center;
// }

// if (rand.nextBoolean()) {
// Corridor corridor1 = new Corridor(Math.round(prevCenter.x),
// Math.round(room2.center.x),
// Math.round(prevCenter.y), true,this);
// corridor1.load();
// corridors.add(corridor1);
// Corridor corridor2 = new Corridor(Math.round(prevCenter.y),
// Math.round(room2.center.y),
// Math.round(room2.center.x), false,this);
// corridor2.load();
// corridors.add(corridor2);
// } else {
// Corridor corridor1 = new Corridor(Math.round(prevCenter.y),
// Math.round(room2.center.y),
// Math.round(prevCenter.x), false,this);
// corridor1.load();
// corridors.add(corridor1);
// Corridor corridor2 = new Corridor(Math.round(prevCenter.x),
// Math.round(room2.center.x),
// Math.round(room2.center.y), true,this);
// corridor2.load();
// corridors.add(corridor2);
// }
// roomCenters.add(room.center);

// }

// private void corridorCreator(SpriteBatch batch) {
// // System.out.println(floors.size());
// // for (Floor floor : floors) {
// // batch.draw(floor.getSprite(), floor.position.x, floor.position.y);
// // }
// for (Corridor corridor : corridors) {
// corridor.draw(batch);
// }
// }

// private void horizontalCorridor(float x1, float x2, float y) {
// y = y - 5;
// x1 = x1 - 5;
// x2 = x2 - 5;
// for (float x = Math.min(x1, x2); x <= Math.max(x1, x2); x += Tile.DIM) {
// Floor floor = new Floor(x, y, 0); // Ajuste de posición
// floors.add(floor);
// }
// }

// private void verticalCorridor(float y1, float y2, float x) {
// x = x - 5;
// y1 = y1 - 5;
// y2 = y2 - 5;
// for (float y = Math.min(y1, y2); y <= Math.max(y1, y2); y += Tile.DIM) {
// Floor floor = new Floor(x, y, 0); // Ajuste de posición
// floors.add(floor);
// }
