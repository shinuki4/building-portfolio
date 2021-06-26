package v1.building;

import akka.actor.ActorSystem;
import play.libs.concurrent.CustomExecutionContext;

import javax.inject.Inject;

/**
 * Custom execution context wired to "post.repository" thread pool
 */
public class BuildingExecutionContext extends CustomExecutionContext {

    @Inject
    public BuildingExecutionContext(ActorSystem actorSystem) {
        super(actorSystem, "post.repository");
    }
}
