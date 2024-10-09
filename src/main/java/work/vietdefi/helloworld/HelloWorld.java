package work.vietdefi.helloworld;


import org.apache.log4j.xml.DOMConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HelloWorld {
    public static final Logger logger = LoggerFactory.getLogger("debug");
    public static void main(String[] args){
        DOMConfigurator.configure("config/log/log4j.xml");
        logger.info("Hello world, Java");
    }
}
