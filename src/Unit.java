public interface Unit {
    void setWriter(Writer writer);
    Writer getWriter();
    void count(Counter counter);
    int count();
    void validate();
    void write();
}
