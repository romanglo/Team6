package boundaries;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CatalogItemRow {

	private Integer m_id;
	private String m_name;
	private String m_type;
	private Double m_price;
	private Image m_image;
	
	
	
	public CatalogItemRow(Integer m_id, String m_name, String m_type, Double m_price, Image m_image) {
		super();
		this.m_id = m_id;
		this.m_name = m_name;
		this.m_type = m_type;
		this.m_price = m_price;
		this.m_image = ResizeImage(m_image);;
	}



	public CatalogItemRow(int m_id, String m_name, String m_type, double m_price) {
		super();
		this.m_id = m_id;
		this.m_name = m_name;
		this.m_type = m_type;
		this.m_price = m_price;
		this.m_image = ResizeImage(new Image(getClass().getResourceAsStream("/boundaries/images/Zerli_Headline.jpg")));
	}
	
	
	private Image ResizeImage(Image imageToResize)
	{
		BufferedImage imageReader = SwingFXUtils.fromFXImage(imageToResize, null);
		BufferedImage newImage = ImageNewScale(imageReader , 80 , 80);
		Image finalImage = SwingFXUtils.toFXImage(newImage, null);
		return finalImage;
	}
	
	
	private BufferedImage ImageNewScale(BufferedImage imageToScale , int newScaleWidth , int newScaleHeight)
	{
		BufferedImage scaledImage = null;
        if (imageToScale != null) {
            scaledImage = new BufferedImage(newScaleWidth, newScaleHeight, imageToScale.getType());
            Graphics2D graphics2D = scaledImage.createGraphics();
            graphics2D.drawImage(imageToScale, 0, 0, newScaleWidth, newScaleHeight, null);
            graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2D.dispose();
        }
        return scaledImage;
	}

	
	public Integer getM_id() {
		return m_id;
	}
	
	
	public String getId(){
	    return m_id.toString();
	}
	
	
	public void setM_id(Integer m_id) {
		this.m_id = m_id;
	}
	
	
	
	public String getM_name() {
		return m_name;
	}
	
	
	public String getName(){
	    return m_name.toString();
	}
	
	
	public void setM_name(String m_name) {
		this.m_name = m_name;
	}
	

	public String getM_type() {
		return m_type;
	}
	
	
	public String getType(){
	    return m_type.toString();
	}
	
	
	public void setM_type(String m_type) {
		this.m_type = m_type;
	}
	
	
	public Double getM_price() {
		return m_price;
	}

	
	public String getPrice(){
	    return m_price.toString();
	}
	
	
	public void setM_price(Double m_price) {
		this.m_price = m_price;
	}
	
	
	public Image getM_image() {
		return m_image;
	}
	
	
	public ImageView getImage()
	{
		return new ImageView(m_image);
	}
	
	
	public void setM_image(Image m_image) {
		this.m_image = ResizeImage(m_image);
	}
	
	
	@Override
	public String toString() {
		return "CatalogItemRaw [m_id=" + m_id + ", m_name=" + m_name + ", m_type=" + m_type + ", m_price=" + m_price
				+ ", m_image=" + m_image + "]";
	}
	
}
