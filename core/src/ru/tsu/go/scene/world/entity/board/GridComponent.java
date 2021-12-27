package ru.tsu.go.scene.world.entity.board;

import com.badlogic.ashley.core.Component;
import lombok.Data;

@Data
public class GridComponent implements Component {

    public int entityId;
    public int[] grid;
}
