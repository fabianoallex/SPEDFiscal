package sped.lcdpr.v0013;

import sped.core.Block;
import sped.core.Context;

public class Block0 extends Block {
    public static String BLOCK_NAME = "0";
    public Block0(Context context) {
        super(BLOCK_NAME, context);
    }

    public Register0010 add0010() {
        return (Register0010) this.addNamedRegister(Register0010.class);
    }

    public Register0030 add0030() {
        return (Register0030) this.addNamedRegister(Register0030.class);
    }

    public Register0040 add0040() {
        return (Register0040) this.addNamedRegister(Register0040.class);
    }

    public Register0050 add0050() {
        return (Register0050) this.addNamedRegister(Register0050.class);
    }
}
