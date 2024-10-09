import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Random;
import java.io.FileOutputStream;

public class BMPGenerator {
	int xpix;
	int ypix;
	ArrayList<Integer> header = new ArrayList<Integer>();	
	
	
	BMPGenerator(int x, int y) {
		xpix = x;
		ypix = y;		
		
		
	}
	public int makeBMP(float[][] map, ArrayList<RouteNode> path) {
/*				int[] bytes = {
				0x42, 0x4D, 		//BM header
				0x46, 0x00, 0x00, 0x00, //size = 54 byte for header + pixel data (multiple of 4)
										// = 54 + 4*antall pix
				0x99, 0x99, 0x99, 0x99, //my signature, lol
				0x36, 0x00, 0x00, 0x00, //offset until pixel data
				0x28, 0x00, 0x00, 0x00, //number of bytes in the rest of the header (DIB)
				0x02, 0x00, 0x00, 0x00, //pixel width of image 
				0x02, 0x00, 0x00, 0x00, //pixel height of image 
				0x01, 0x00, 			//1 color plane. No idea what that means??
				0x18, 0x00, 			//18 bits per pixel
				0x00, 0x00, 0x00, 0x00, //compression IGNORE
				0x10, 0x00, 0x00, 0x00, //size of bitmap data (10 = hex 16) = 4*antall pix
				0x13, 0x0B, 0x00, 0x00, //print resolution of image
				0x13, 0x0B, 0x00, 0x00, //print resolution of image
				0x00, 0x00, 0x00, 0x00, //number of colours in palette, means??
				0x00, 0x00, 0x00, 0x00, //number of important colors. 0 is good.
				
				0x00, 0x00, 0xFF, 	//pix 1
				0xFF, 0xFF, 0xFF, 	//pix 2
				0x00, 0x00, 		//padding to a multiple of 4
				0xFF, 0x00, 0x00, 	//pix 3
				0x00, 0xFF, 0x00, 	//pix 4
				0x00, 0x00};		//padding to a multiple of 4 */
		
		header.add(0x42);
		header.add(0x4D); //BM header
		
		int pixelzoomfactor = 15; //all pixels are enlarged by this factor
		
		int pad = (pixelzoomfactor*xpix*3)%4;
		int xpixWithPad =pixelzoomfactor*xpix*3;
		if(pad!=0) //we need to pad xlines
			xpixWithPad += (4-pad);
		
		int size = xpixWithPad*ypix*pixelzoomfactor;
		header.add( (size+54)%256 );  	//size = 54 byte for header + pixel data (multiple of 4)
		header.add( (size+54)/256 );				// = 54 + 4*antall pix		
		header.add( ((size+54)/256)%256 );
		header.add( ((size+54)/256)/256 );
		
		header.add(0xAA);	//signature
		header.add(0xAA);
		header.add(0xAA);
		header.add(0xAA);
				
		header.add(0x36);	//offset until pixel data
		header.add(0x00);
		header.add(0x00);
		header.add(0x00);	
		
		header.add(0x28);	//number of bytes in the rest of the header (DIB)
		header.add(0x00);
		header.add(0x00);
		header.add(0x00);
		
		header.add( (xpix*pixelzoomfactor)%256 );	//pixel width of image
		header.add( (xpix*pixelzoomfactor)/256 );
		header.add(0x00);
		header.add(0x00);
		
		header.add( (ypix*pixelzoomfactor)%256 );	//pixel height of image 
		header.add( (ypix*pixelzoomfactor)/256 );
		header.add(0x00);
		header.add(0x00);
		
		header.add(0x01);	//1 color plane. No idea what that means??
		header.add(0x00);
		
		header.add(0x18);	//24 bits per pixel
		header.add(0x00);
		
		header.add(0x00);	//compression IGNORE
		header.add(0x00);
		header.add(0x00);
		header.add(0x00);
	
		header.add( (size)%256 );  	////size of bitmap data (10 = hex 16) = 3*antall pix
		header.add( (size)/256 );					
		header.add( ((size)/256)%256 );
		header.add( ((size)/256)/256 );
		
		header.add(0x00);	//print resolution of image 
		header.add(0x00);
		header.add(0x00);
		header.add(0x00);
		
		header.add(0x00);	//print resolution of image
		header.add(0x00);
		header.add(0x00);
		header.add(0x00);
			
		header.add(0x00);	//number of colours in palette, means??
		header.add(0x00);
		header.add(0x00);
		header.add(0x00);
		
		header.add(0x00);	//number of important colors. 0 is good
		header.add(0x00);
		header.add(0x00);
		header.add(0x00);
		
				
		char pathMap[][] = new char[map.length][map[0].length];
		for (int i=0;i<path.size();i++)
			pathMap[path.get(i).getx()][path.get(i).gety()] = 'r';
		
		pathMap[path.get(0).getx()][path.get(0).gety()] = 's';
		pathMap[path.get(path.size()-1).getx()][path.get((path.size()-1)).gety()] = 'e';
		
		int repeaty=0;
		
		for (int i=0;i<ypix;) //skal endre ypix til map[0].length
		{
			int repeatx =0;
			if(repeaty == pixelzoomfactor) 
			{
				repeaty =0;
				i++;
			}
			else 
			{
				for(int j=0;j<xpix;)  //skal endre xpix til map.length
				{
					if (repeatx == pixelzoomfactor) 
					{ //making every pixel repeat in xline
						repeatx=0;
						j++;
					}	
					else 
					{
						if (map[j][i] >=(float)1) 
						{ //lag murer sorte
							//System.out.println("I for loop, sjekker om map[j][i] har murer. j="+j+", i="+i);
							header.add(0x00);
							header.add(0x00);
							header.add(0x00);
						}
						else 
						{
							int color = (int) (map[j][i]*(float)256) ; //lav = green, high=red
							if (pathMap[j][i] == 'r') {
								header.add(0xC8); //B
								header.add(0xFF-color); //G
								header.add(color); //R							
							}
							else if (pathMap[j][i] == 's' || pathMap[j][i] == 'e') {
								header.add(0xFF); //B
								header.add(0xFF-color/5); //G
								header.add(0xFF); //R							
							}
							else {
								header.add(0x00); //B
								header.add(0xFF-color); //G
								header.add(color); //R	
							}
											
						}
						repeatx++;
					}
								
				}
				repeaty++;
				if(pad!=0) 
				{
					
					for(int k=0;k<4-pad;k++) 
					{					
						header.add(0x00);	//padding up to a multiple of 4 bytes
					}
				
				}
			}	
			
		}
		System.out.println("Padder hver linje "+ (4-pad) +" ganger, i "+ypix*pixelzoomfactor+"pixelrader.");
		try {
			OutputStream outputStream = new FileOutputStream("Pathmap"+xpix+"x"+ypix+" "+pixelzoomfactor+ "xzoom.bmp");
		    for (int k=0;k<header.size();k++) {
		    	outputStream.write(header.get(k)); 	
		    	outputStream.flush();	}
		}
		catch (Exception e) { 	}
		finally {     }
		System.out.println("Output done.");
		return 1;
	}	
}