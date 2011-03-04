package at.ac.univie.philo.mmr.client.gui;

import net.sourceforge.htmlunit.corejs.javascript.ast.ParenthesizedExpression;
import at.ac.univie.philo.mmr.client.gui.UniverseDetailsForm.DetailStyle;
import at.ac.univie.philo.mmr.shared.semantic.Individual;
import at.ac.univie.philo.mmr.shared.semantic.World;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.XMLParser;

public class IndividualDetailsForm extends Composite {

	private static IndividualDetailsFormUiBinder uiBinder = GWT
			.create(IndividualDetailsFormUiBinder.class);

	interface IndividualDetailsFormUiBinder extends
			UiBinder<Widget, IndividualDetailsForm> {
	}
	
	private Resources res;
//	private SingleUploader defaultUploader;
	private FlowPanel flowPanel;
	private Individual individual;
	private World world;
	private DialogBox db = new DialogBox();
	private MainScreen parentWidget;
	
	public IndividualDetailsForm(final Individual i, World w, final MainScreen parentWidget) {
		res = GWT.create(Resources.class);
		this.individual = i;
		this.world = w;
		this.parentWidget = parentWidget;
		
		initWidget(uiBinder.createAndBindUi(this));
		mainVPanel.setWidth("100%");
		mainVPanel.setHeight("100%");
//		indiIconContainer.setWidth("100%");
//		indiIconContainer.setHeight("100%");
		indiNameHelpButton.setTitle("What is the ordinary name of an Individual?");
		indiNameContainer.setCellVerticalAlignment(indiNameHelpButton, HasVerticalAlignment.ALIGN_MIDDLE);
		indiNameContainer.setCellVerticalAlignment(indiNameHtmlPanel, HasVerticalAlignment.ALIGN_MIDDLE);
		indiNameContainer.setCellVerticalAlignment(indiNameLabel, HasVerticalAlignment.ALIGN_MIDDLE);
		indiIconContainer.setCellVerticalAlignment(indiIconHtmlPanel, HasVerticalAlignment.ALIGN_MIDDLE);
		indiIconContainer.setCellVerticalAlignment(indiIconHolder, HasVerticalAlignment.ALIGN_MIDDLE);
		indiIconContainer.setCellVerticalAlignment(changeImage, HasVerticalAlignment.ALIGN_MIDDLE);
		indiIconContainer.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		indiNameLabel.setText(i.getName());
		individualName.setInnerText(i.getName());
		changeImage.setTitle("Change the current icon by uploading/selecting another one.");
		changeImage.addClickHandler(changeIconClickHandler);
		
		Image icon = i.getIcon();
		if (icon == null) {
			icon = new Image(res.defaultIndividual());
		}
		icon.addClickHandler(changeIconClickHandler);
		
		indiIconHolder.add(icon);
		
		
		
	}
	
	private ClickHandler changeIconClickHandler = new ClickHandler() {
		
		@Override
		public void onClick(ClickEvent event) {
			showIconSelector(individual);
		}
	};
	
	protected void showIconSelector(Individual i) {
		db.show();
		db.setText("Select Icon for Individual "+i.getName());
//		defaultUploader = new SingleUploader();
//		defaultUploader.avoidRepeatFiles(true);
//		defaultUploader.addOnFinishUploadHandler(onFinishUploaderHandler);
//		
		//retrieve all images from server -> put them as a pushbutton into flowpanel
		
		VerticalPanel vp = new VerticalPanel();
		Button abort = new Button("Abort");
		abort.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				db.hide();
			}
		});
		
		flowPanel = new FlowPanel();
		
//		vp.add(defaultUploader);
		vp.add(flowPanel);
		vp.add(abort);
		vp.addStyleName(style.distance());
		db.add(vp);
		db.center();
		db.setAutoHideEnabled(true);
		
	}
	
	// Load the image in the document and in the case of success attach it to the viewer
//	  private IUploader.OnFinishUploaderHandler onFinishUploaderHandler = new IUploader.OnFinishUploaderHandler() {
//	    public void onFinish(IUploader uploader) {
//	      if (uploader.getStatus() == Status.SUCCESS) {
//
//	        new PreloadedImage(uploader.fileUrl(), showImage);
//	        
//	        // The server can send information to the client.
//	        // You can parse this information using XML or JSON libraries
//	        Document doc = XMLParser.parse(uploader.getServerResponse());
//	        String size = Utils.getXmlNodeValue(doc, "size");
//	        String type = Utils.getXmlNodeValue(doc, "ctype");
//	        if (!type.contains("image")) {
//	        	Window.alert("You have uploaded a non-image file (type: "+type+" )");
//	        }
//	        System.out.println(size + " " + type);
//	      }
//	    }
//	  };
//
//	  // Attach an image to the pictures viewer
//	  private OnLoadPreloadedImageHandler showImage = new OnLoadPreloadedImageHandler() {
//	    public void onLoad(PreloadedImage image) {
//	      image.setWidth("45px");
//	      image.addClickHandler(changeIconClickHandler);
//	      flowPanel.add(image);
//	      indiIconHolder.clear();
//	      indiIconHolder.add(image);
//	      individual.setIcon(image);
//	      db.hide();
//	      updateModel();
//
//	    }
//	  };
	
	@UiField
	VerticalPanel mainVPanel;
	@UiField
	HorizontalPanel indiNameContainer;
	@UiField
	HTMLPanel indiNameHtmlPanel;
	@UiField
	Label indiNameLabel;
	@UiField
	Button indiNameHelpButton;
	@UiField
	HorizontalPanel indiIconContainer;
	@UiField
	HTMLPanel indiIconHtmlPanel;
	@UiField
	SimplePanel indiIconHolder;
	@UiField
	SpanElement individualName;
	@UiField
	Button changeImage;
	@UiField
	DetailStyle style;

	@UiHandler("indiNameHelpButton")
	void onClick(ClickEvent e) {
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setText("What is the ordinary name of an Individual?");
		VerticalPanel vp = new VerticalPanel();
		vp.addStyleName(style.distance());
		HTMLPanel hpanel = new HTMLPanel("<div>The ordinary name is not a name in our artificial language. It is just a name we usually refer to when we speak about an individual. For humans it usually is easier to associate something with a name like 'Mr. Spok' than the constant name in the artificial language 'a_1'. Thus, this makes life easier when constructing extensions of predicates or when reading comments about the evaluation process.</div>");
		Button ok = new Button("OK");
		ok.addStyleName(style.distance());
		ok.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();
			}
		});
		vp.add(hpanel);
		vp.add(ok);	
		dialogBox.add(vp);
		dialogBox.show();
		dialogBox.setAnimationEnabled(true);
		dialogBox.center();
	}

	protected void updateModel() {
		parentWidget.updateModel();
	}


}
