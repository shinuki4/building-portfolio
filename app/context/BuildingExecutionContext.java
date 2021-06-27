package context;

import akka.actor.ActorSystem;
import play.libs.concurrent.CustomExecutionContext;

import javax.inject.Inject;

/**
 * Custom execution context wired to "building.repository" thread pool
 */
public class BuildingExecutionContext extends CustomExecutionContext {

    @Inject
    public BuildingExecutionContext(ActorSystem actorSystem) {
        super(actorSystem, "building.repository");
    }
}
