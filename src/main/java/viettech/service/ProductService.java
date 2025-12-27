package viettech.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viettech.dao.LaptopDAO;
import viettech.dao.PhoneDAO;
import viettech.dao.TabletDAO;

public class ProductService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final PhoneDAO phoneDAO ;
    private final LaptopDAO laptopDAO ;
    private final TabletDAO tabletDAO ;
    public ProductService() {
        this.phoneDAO = new PhoneDAO();
        this.laptopDAO = new LaptopDAO();
        this.tabletDAO = new TabletDAO();
    }
    //1 is phone 2 is tablet 3 is laptop
    public void insertProduct(int type,String name,String price,String image){}


}
