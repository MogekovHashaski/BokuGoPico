package ru.tsu.go.scene.world.entity.util;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import ru.tsu.go.scene.world.entity.TransformComponent;

import java.util.Comparator;

@Value
@RequiredArgsConstructor
public class ZComparator implements Comparator<Entity> {

    ComponentMapper<TransformComponent> mapper;

    public ZComparator() {
        mapper = ComponentMapper.getFor(TransformComponent.class);
    }

    @Override
    public int compare(final Entity entityA, final Entity entityB) {
        return (int) Math.signum(mapper.get(entityB).position.z - mapper.get(entityA).position.z);
    }
}
