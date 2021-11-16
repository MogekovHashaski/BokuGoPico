package ru.tsu.go.scene.hci;


import com.badlogic.ashley.core.Component;
import lombok.Data;

@Data
public class ClickComponent implements Component {
    public ClickListener listener;
}
