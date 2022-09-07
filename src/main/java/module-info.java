module dev.mccue.rosie.jetty {
    requires transitive jetty.servlet.api;
    requires transitive dev.mccue.rosie;
    requires transitive org.eclipse.jetty.server;

    exports dev.mccue.rosie.jetty;
}