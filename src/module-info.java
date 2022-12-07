module SPEDFiscalLib {
    requires java.xml;
    requires org.objectweb.asm;
    requires java.scripting;
    requires org.openjdk.nashorn;

    exports sped.core;
    exports sped.lcdpr.v0013;
}