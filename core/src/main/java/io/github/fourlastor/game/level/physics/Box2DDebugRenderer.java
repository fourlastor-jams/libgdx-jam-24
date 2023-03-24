package io.github.fourlastor.game.level.physics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.Transform;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.badlogic.gdx.physics.box2d.joints.PulleyJoint;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import java.util.Iterator;

class Box2DDebugRenderer implements Disposable {

    /**
     * the immediate mode renderer to output our debug drawings
     **/
    protected ShapeRenderer renderer;

    /**
     * vertices for polygon rendering
     **/
    private static final Vector2[] vertices = new Vector2[1000];

    private static final Vector2 lower = new Vector2();
    private static final Vector2 upper = new Vector2();

    private static final Array<Body> bodies = new Array<>();
    private static final Array<Joint> joints = new Array<>();

    private final boolean drawBodies;
    private final boolean drawJoints;
    private final boolean drawAABBs;
    private final boolean drawInactiveBodies;
    private final boolean drawVelocities;
    private final boolean drawContacts;

    public Box2DDebugRenderer() {
        this(true, true, false, true, false, true);
    }

    public Box2DDebugRenderer(
            boolean drawBodies,
            boolean drawJoints,
            boolean drawAABBs,
            boolean drawInactiveBodies,
            boolean drawVelocities,
            boolean drawContacts) {
        // next we setup the immediate mode renderer
        renderer = new ShapeRenderer();

        // initialize vertices array
        for (int i = 0; i < vertices.length; i++) vertices[i] = new Vector2();

        this.drawBodies = drawBodies;
        this.drawJoints = drawJoints;
        this.drawAABBs = drawAABBs;
        this.drawInactiveBodies = drawInactiveBodies;
        this.drawVelocities = drawVelocities;
        this.drawContacts = drawContacts;
    }

    /**
     * This assumes that the projection matrix has already been set.
     */
    public void render(World world, Matrix4 projMatrix) {
        renderer.setProjectionMatrix(projMatrix);
        renderBodies(world);
    }

    public final Color SHAPE_NOT_ACTIVE = new Color(0.5f, 0.5f, 0.3f, 1);
    public final Color SHAPE_STATIC = new Color(0.5f, 0.9f, 0.5f, 1);
    public final Color SHAPE_KINEMATIC = new Color(0.5f, 0.5f, 0.9f, 1);
    public final Color SHAPE_OTHER = new Color(0.2f, 0.2f, 0.2f, 1);
    public final Color JOINT_COLOR = new Color(0.5f, 0.8f, 0.8f, 1);
    public final Color AABB_COLOR = new Color(1.0f, 0, 1.0f, 1f);
    public final Color VELOCITY_COLOR = new Color(1.0f, 0, 0f, 1f);

    private void renderBodies(World world) {
        renderer.begin(ShapeRenderer.ShapeType.Line);

        if (drawBodies || drawAABBs) {
            world.getBodies(bodies);
            for (Iterator<Body> iter = bodies.iterator(); iter.hasNext(); ) {
                Body body = iter.next();
                if (body.isActive() || drawInactiveBodies) renderBody(body);
            }
        }

        if (drawJoints) {
            world.getJoints(joints);
            for (Iterator<Joint> iter = joints.iterator(); iter.hasNext(); ) {
                Joint joint = iter.next();
                drawJoint(joint);
            }
        }
        renderer.end();
        if (drawContacts) {
            renderer.begin(ShapeRenderer.ShapeType.Point);
            for (Contact contact : world.getContactList()) drawContact(contact);
            renderer.end();
        }
    }

    protected void renderBody(Body body) {
        Transform transform = body.getTransform();
        for (Fixture fixture : body.getFixtureList()) {
            if (drawBodies) {
                drawShape(fixture, transform, getColorByBody(body));
                if (drawVelocities) {
                    Vector2 position = body.getPosition();
                    drawSegment(position, body.getLinearVelocity().add(position), VELOCITY_COLOR);
                }
            }

            if (drawAABBs) {
                drawAABB(fixture, transform);
            }
        }
    }

    private Color getColorByBody(Body body) {
        if (!body.isActive()) return SHAPE_NOT_ACTIVE;
        else if (body.getType() == BodyDef.BodyType.StaticBody) return SHAPE_STATIC;
        else if (body.getType() == BodyDef.BodyType.KinematicBody) return SHAPE_KINEMATIC;
        else return SHAPE_OTHER;
    }

    private void drawAABB(Fixture fixture, Transform transform) {
        if (fixture.getType() == Shape.Type.Circle) {

            CircleShape shape = (CircleShape) fixture.getShape();
            float radius = shape.getRadius();
            vertices[0].set(shape.getPosition());
            transform.mul(vertices[0]);
            lower.set(vertices[0].x - radius, vertices[0].y - radius);
            upper.set(vertices[0].x + radius, vertices[0].y + radius);

            // define vertices in ccw fashion...
            vertices[0].set(lower.x, lower.y);
            vertices[1].set(upper.x, lower.y);
            vertices[2].set(upper.x, upper.y);
            vertices[3].set(lower.x, upper.y);

            drawSolidPolygon(vertices, 4, AABB_COLOR, true);
        } else if (fixture.getType() == Shape.Type.Polygon) {
            PolygonShape shape = (PolygonShape) fixture.getShape();
            int vertexCount = shape.getVertexCount();

            shape.getVertex(0, vertices[0]);
            lower.set(transform.mul(vertices[0]));
            upper.set(lower);
            for (int i = 1; i < vertexCount; i++) {
                shape.getVertex(i, vertices[i]);
                transform.mul(vertices[i]);
                lower.x = Math.min(lower.x, vertices[i].x);
                lower.y = Math.min(lower.y, vertices[i].y);
                upper.x = Math.max(upper.x, vertices[i].x);
                upper.y = Math.max(upper.y, vertices[i].y);
            }

            // define vertices in ccw fashion...
            vertices[0].set(lower.x, lower.y);
            vertices[1].set(upper.x, lower.y);
            vertices[2].set(upper.x, upper.y);
            vertices[3].set(lower.x, upper.y);

            drawSolidPolygon(vertices, 4, AABB_COLOR, true);
        }
    }

    private static final Vector2 t = new Vector2();
    private static final Vector2 axis = new Vector2();

    private static final Color ACTIVE_WEAPON = new Color(0xa53030FF);
    private static final Color DISABLED_COLOR = new Color(0x202e37FF);

    private void drawShape(Fixture fixture, Transform transform, Color color) {
        if (fixture.getFilterData().maskBits == BodyData.Mask.WEAPON.bits) {
            color = ACTIVE_WEAPON;
        }
        if (fixture.getFilterData().maskBits == BodyData.Mask.DISABLED.bits) {
            color = DISABLED_COLOR;
        }
        if (fixture.getType() == Shape.Type.Circle) {
            CircleShape circle = (CircleShape) fixture.getShape();
            t.set(circle.getPosition());
            transform.mul(t);
            drawSolidCircle(
                    t,
                    circle.getRadius(),
                    axis.set(transform.vals[Transform.COS], transform.vals[Transform.SIN]),
                    color);
            return;
        }

        if (fixture.getType() == Shape.Type.Edge) {
            EdgeShape edge = (EdgeShape) fixture.getShape();
            edge.getVertex1(vertices[0]);
            edge.getVertex2(vertices[1]);
            transform.mul(vertices[0]);
            transform.mul(vertices[1]);
            drawSolidPolygon(vertices, 2, color, true);
            return;
        }

        if (fixture.getType() == Shape.Type.Polygon) {
            PolygonShape chain = (PolygonShape) fixture.getShape();
            int vertexCount = chain.getVertexCount();
            for (int i = 0; i < vertexCount; i++) {
                chain.getVertex(i, vertices[i]);
                transform.mul(vertices[i]);
            }
            drawSolidPolygon(vertices, vertexCount, color, true);
            return;
        }

        if (fixture.getType() == Shape.Type.Chain) {
            ChainShape chain = (ChainShape) fixture.getShape();
            int vertexCount = chain.getVertexCount();
            for (int i = 0; i < vertexCount; i++) {
                chain.getVertex(i, vertices[i]);
                transform.mul(vertices[i]);
            }
            drawSolidPolygon(vertices, vertexCount, color, false);
        }
    }

    private final Vector2 f = new Vector2();
    private final Vector2 v = new Vector2();
    private final Vector2 lv = new Vector2();

    private void drawSolidCircle(Vector2 center, float radius, Vector2 axis, Color color) {
        float angle = 0;
        float angleInc = 2 * (float) Math.PI / 20;
        renderer.setColor(color.r, color.g, color.b, color.a);
        for (int i = 0; i < 20; i++, angle += angleInc) {
            v.set((float) Math.cos(angle) * radius + center.x, (float) Math.sin(angle) * radius + center.y);
            if (i == 0) {
                lv.set(v);
                f.set(v);
                continue;
            }
            renderer.line(lv.x, lv.y, v.x, v.y);
            lv.set(v);
        }
        renderer.line(f.x, f.y, lv.x, lv.y);
        renderer.line(center.x, center.y, 0, center.x + axis.x * radius, center.y + axis.y * radius, 0);
    }

    private void drawSolidPolygon(Vector2[] vertices, int vertexCount, Color color, boolean closed) {
        renderer.setColor(color.r, color.g, color.b, color.a);
        lv.set(vertices[0]);
        f.set(vertices[0]);
        for (int i = 1; i < vertexCount; i++) {
            Vector2 v = vertices[i];
            renderer.line(lv.x, lv.y, v.x, v.y);
            lv.set(v);
        }
        if (closed) renderer.line(f.x, f.y, lv.x, lv.y);
    }

    private void drawJoint(Joint joint) {
        Body bodyA = joint.getBodyA();
        Body bodyB = joint.getBodyB();
        Transform xf1 = bodyA.getTransform();
        Transform xf2 = bodyB.getTransform();

        Vector2 x1 = xf1.getPosition();
        Vector2 x2 = xf2.getPosition();
        Vector2 p1 = joint.getAnchorA();
        Vector2 p2 = joint.getAnchorB();

        if (joint.getType() == JointDef.JointType.DistanceJoint) {
            drawSegment(p1, p2, JOINT_COLOR);
        } else if (joint.getType() == JointDef.JointType.PulleyJoint) {
            PulleyJoint pulley = (PulleyJoint) joint;
            Vector2 s1 = pulley.getGroundAnchorA();
            Vector2 s2 = pulley.getGroundAnchorB();
            drawSegment(s1, p1, JOINT_COLOR);
            drawSegment(s2, p2, JOINT_COLOR);
            drawSegment(s1, s2, JOINT_COLOR);
        } else if (joint.getType() == JointDef.JointType.MouseJoint) {
            drawSegment(joint.getAnchorA(), joint.getAnchorB(), JOINT_COLOR);
        } else {
            drawSegment(x1, p1, JOINT_COLOR);
            drawSegment(p1, p2, JOINT_COLOR);
            drawSegment(x2, p2, JOINT_COLOR);
        }
    }

    private void drawSegment(Vector2 x1, Vector2 x2, Color color) {
        renderer.setColor(color);
        renderer.line(x1.x, x1.y, x2.x, x2.y);
    }

    private void drawContact(Contact contact) {
        WorldManifold worldManifold = contact.getWorldManifold();
        if (worldManifold.getNumberOfContactPoints() == 0) return;
        Vector2 point = worldManifold.getPoints()[0];
        renderer.setColor(getColorByBody(contact.getFixtureA().getBody()));
        renderer.point(point.x, point.y, 0);
    }

    public void dispose() {
        renderer.dispose();
    }
}
