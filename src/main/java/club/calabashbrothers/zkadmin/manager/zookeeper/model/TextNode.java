package club.calabashbrothers.zkadmin.manager.zookeeper.model;

/**
 * Created by liaojiacan on 2017/2/16.
 */
public class TextNode extends ZkNode<String> {

    public TextNode(String path) {
        super(path);
    }

    @Override
    public byte[] getBytes() {
        return getContent()!=null?getContent().getBytes():new byte[0];
    }

    @Override
    public String parse(byte[] data) {
        return new String(data);
    }
}
