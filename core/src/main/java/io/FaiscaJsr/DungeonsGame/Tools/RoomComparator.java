package io.FaiscaJsr.DungeonsGame.Tools;

import java.util.Comparator;

import io.FaiscaJsr.DungeonsGame.MapGenerator.Room.Room;

/**
 * Clase RoomComparator
 */
public class RoomComparator implements Comparator<Room> {

    /**
     * Metodo que compara dos Rooms
     *
     * @param o1 Objeto 1
     * @param o2 Objeto 2
     * @return Retorna la comparacion
     */
    @Override
    public int compare(Room o1, Room o2) {
        return o1.center.x < o2.center.x ? -1 : o1.center.x > o2.center.x ? 1 : 0;
    }

}
