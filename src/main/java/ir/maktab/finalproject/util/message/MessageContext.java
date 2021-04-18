package ir.maktab.finalproject.util.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
@AllArgsConstructor
public  abstract class MessageContext {
    private String from;
    private String to;
    private String subject;
    private String email;
    private String attachment;
    private String fromDisplayName;
    private String emailLanguage;
    private String displayName;
    private String templateLocation;
    private Map<String, Object> context;

    public MessageContext() {
        this.context=new HashMap<>();
    }
    public <T> void init(T context){

    }
    public Object put(String key, Object value) {
        return key ==null ? null : this.context.put(key.intern(),value);
    }
}
