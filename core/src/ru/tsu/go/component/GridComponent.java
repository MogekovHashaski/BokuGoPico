package ru.tsu.go.component;

import com.badlogic.ashley.core.Component;
import lombok.Data;

@Data
public class GridComponent implements Component {

    public int entityId;
    public int[] grid;
}
