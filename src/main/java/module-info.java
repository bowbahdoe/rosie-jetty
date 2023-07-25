import org.jspecify.annotations.NullMarked;

@NullMarked
module dev.mccue.rosie.jetty {
    requires static org.jspecify;

    requires transitive jetty.servlet.api;
    requires transitive dev.mccue.rosie;
    requires transitive org.eclipse.jetty.server;

    exports dev.mccue.rosie.jetty;
}