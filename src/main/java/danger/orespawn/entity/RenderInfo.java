package danger.orespawn.entity;

/**
 * Gold {@code RenderInfo} — client animation state for complex models (e.g. Alien).
 */
public class RenderInfo {
    public float rf1;
    public float rf2;
    public float rf3;
    public float rf4;
    public int ri1;
    public int ri2;
    public int ri3;
    public int ri4;

    public RenderInfo() {
        this.rf1 = this.rf2 = this.rf3 = this.rf4 = 0.0F;
        this.ri1 = this.ri2 = this.ri3 = this.ri4 = 0;
    }
}
