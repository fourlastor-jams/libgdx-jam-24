package io.github.fourlastor.game.level.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import io.github.fourlastor.game.level.reward.RewardStateMachine;
import io.github.fourlastor.game.level.reward.RewardType;
import io.github.fourlastor.game.level.reward.state.Away;
import io.github.fourlastor.game.level.reward.state.Chasing;
import io.github.fourlastor.game.level.reward.state.Inactive;

public class Reward implements Component {
    public final RewardType type;

    public Reward(RewardType type) {
        this.type = type;
    }

    public static class PickUp implements Component {
        public final Entity player;

        public PickUp(Entity player) {
            this.player = player;
        }
    }

    public static class State implements Component {
        public final RewardStateMachine stateMachine;
        public final Away away;
        public final Chasing chasing;
        public final Inactive inactive;

        public State(RewardStateMachine stateMachine, Away away, Chasing chasing, Inactive inactive) {
            this.stateMachine = stateMachine;
            this.away = away;
            this.chasing = chasing;
            this.inactive = inactive;
        }
    }
}
