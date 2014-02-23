import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class ecc {


	public static BigInteger multiply(BigInteger z, BigInteger a, int x, int y) {

		BigInteger result = new BigInteger("0");

                if(z.equals(new BigInteger("0")) || a.equals(new BigInteger("0")))
                {
                    return result;
                }

		BigInteger k = new BigInteger("1");
		for (int i = 0; i < 255; i++) {
			if ((a.intValue() & k.intValue()) != 0) {
				result = result.xor(z.shiftLeft(i));
			}
			 k = k.shiftLeft(1);
		}
		while (result.intValue() > x - 1) {

			BigInteger b = new BigInteger(Integer.toString(y));
			BigInteger d = result;
			
			while ((d.intValue() / 2) > x) {
				d = new BigInteger(Integer.toString((d.intValue() / 2)));
				b = b.shiftLeft(1);
			}

			result = result.xor(b);
		}
		result = result.mod(new BigInteger(Integer.toString(x)));

		return result;
	}

	public static void main(String[] args) throws Exception {
		// ecc q= new ecc();
		BigInteger g;
		BigInteger a;
		int[] flag;
		int flag1;
                int flag3 = 0;

		InputStreamReader converter = new InputStreamReader(System.in);
		BufferedReader in = new BufferedReader(converter);
		System.out.println("enter the value of m");
		int t = Integer.parseInt(in.readLine());
		int y = 0;
		int x = (int) Math.pow(2, t);
		flag = new int[x];
		a = new BigInteger("2");
		System.out.println("enter the irreducble polynomial");
		String s = in.readLine();
		StringTokenizer st = new StringTokenizer(s, " ");
		while (st.hasMoreElements()) {
			y = (int) (y + Math.pow(2, Integer.parseInt(st.nextToken())));
		}

                System.out.println("List of the generator(s) : ");

		for (; a.compareTo(new BigInteger(Integer.toString(x))) < 0; a = a
				.add(BigInteger.ONE)) {
			
                        flag1 = 0;
			for (int i = 1; i < x; i++) {
				flag[i] = 0;
			}
			BigInteger z = a;
			for (int i = 0; i < x; i++) {
				z = multiply(z, a, x, y);
				//System.out.println(z);
                                if (flag[z.intValue()] == 1)
                                    break;
                                flag[z.intValue()] = 1;
			}
			for (int i = 1; i < x; i++) {
                            if (flag[i] == 0) {
                                flag1 = 1;
                                break;
                            }
			}
			if (flag1 == 0) {
                            flag3 = 1;
                            System.out.println(a);
			}

		}

                if(flag3 == 0)
                {
                    System.out.println("No generator present. Exiting.");
                    return;
                }
                
		System.out.println("Select any of the above generator : ");
		int selectGen = Integer.parseInt(in.readLine());

		g = new BigInteger(Integer.toString(selectGen));
		int i, j;
		System.out.println("enter the value of i");
		i = Integer.parseInt(in.readLine());
		System.out.println("enter the value of j");
		j = Integer.parseInt(in.readLine());
		BigInteger gi = new BigInteger(i+"");
		BigInteger gj = new BigInteger(j+"");

                BigInteger genPower[] = new BigInteger[x];
		genPower[0] = new BigInteger("1");
		genPower[1] = g;
                // This is for coordinate 0 which cannot be represented in powers of generator
                genPower[x-1] = new BigInteger("0");

		BigInteger gNew = g;

		for(int k = 2;k<x-1;k++) {
			gNew = multiply(gNew, g, x, y);
			genPower[k] = gNew;
                        //System.out.println(gNew);
		}

		a = new BigInteger("0");

                ArrayList<Integer> xpoint = new ArrayList<Integer>();
                ArrayList<Integer> ypoint = new ArrayList<Integer>();
                xpoint.add(0,0);
                ypoint.add(0,0);

                System.out.println("List of Points on the curve : ");
		int countp = 0;
		for (; a.compareTo(new BigInteger(Integer.toString(x))) < 0; a = a
				.add(BigInteger.ONE)) {
			BigInteger b = new BigInteger("0");
			for (; b.compareTo(new BigInteger(Integer.toString(x))) < 0; b = b
					.add(BigInteger.ONE)) {
				if (curve(a,b, gi, gj,g, x, y,genPower)) {
                                    
                                        int xIndex = -1, yIndex = -1;

                                        ++countp;

                                        for(int k=0;k<x;k++) {
                                                if(a.equals(genPower[k])) {
                                                        xIndex = k;
                                                }
                                                if(b.equals(genPower[k])) {
                                                        yIndex = k;
                                                }
                                        }
                                        xpoint.add(countp,xIndex);
                                        ypoint.add(countp,yIndex);
					System.out.println(countp + " : (" + a + "," + b + ")");
				}
			}
		}

                ++countp;
                xpoint.add(countp,0);
                ypoint.add(countp,0);

                System.out.println(countp + " : Infinity");

		System.out.println("Total no. of points on the curve : " + countp);

                System.out.println("List of generators : ");

                int countg = 0;
                int flag2[] = new int[countp];
                int xDouble = 0, yDouble = 0, xAdd = 0, yAdd = 0;
                //int pIndex = 0;

                ArrayList<Integer> genIndex = new ArrayList<Integer>();
                genIndex.add(0,0);

                for(int k=1; k <= countp-1; k++)
                {

                    flag1 = 0;

                    xDouble = 0;
                    yDouble = 0;
                    xAdd = 0;
                    yAdd = 0;
                    for (i = 0; i < countp; i++) {
			flag2[i] = 0;
                    }

                    flag2[k-1] = 1;

                    //System.out.println("\n" + k + "  " + genPower[xpoint.get(k)] + "  " + genPower[ypoint.get(k)]);

                    int x1 = xpoint.get(k);
                    int y1 = ypoint.get(k);

                    if(x1 == x-1)
                    {   flag2[countp-1] = 1;
                        continue;
                    }else
                    {
                        xDouble = xpointDouble(x1,y1,g,gi,gj,x,y,genPower);
                        yDouble = ypointDouble(x1,y1,g,gi,gj,x,y,genPower,xDouble);

                        for (i = 1; i <= countp-1; i++) {
			if (xpoint.get(i).equals(xDouble) && ypoint.get(i).equals(yDouble))
                            flag2[i-1] = 1;
                        }

                        xAdd = xDouble;
                        yAdd = yDouble;
                    }

                    //System.out.println(xDouble + "  " + yDouble + " : " + genPower[xDouble] + " , " + genPower[yDouble] + " - Double");
                    //System.out.println(genPower[xAdd] + " , " + genPower[yAdd]);

                    int l;
                    for(l=1; l <= countp-3; l++)
                    {
                        if(genPower[xAdd].equals(genPower[x1]))// & (genPower[x1].xor(genPower[y1])).equals(genPower[yAdd]))
                        {
                            flag2[countp-1] = 1;
                            continue;
                        }else{
                            int temp = xAdd;
                            xAdd = xpointAdd(x1,y1,xAdd,yAdd,g,gi,x ,y ,genPower);
                            yAdd = ypointAdd(x1,y1,temp,yAdd,g ,gi,x ,y ,genPower,xAdd);

                            //System.out.println(xAdd + "  " + yAdd + " : " + genPower[xAdd] + " , " + genPower[yAdd]);

                            for (i = 1; i <= countp-1; i++) {
                            if (xpoint.get(i).equals(xAdd) && ypoint.get(i).equals(yAdd))
                                flag2[i-1] = 1;
                            }
                        }
                    }

                    for ( i = 0; i < countp-1; i++) {
                        if (flag2[i] == 0) {
                            flag1 = 1;
                                break;
			}
                    }

                    if (flag1 == 0) {

                        countg++;

                        genIndex.add(countg,k);

                        System.out.println(countg + " : (" + genPower[x1] + "," + genPower[y1] + ")");
                    }                    
                }

                if(countg == 0)
                {
                    System.out.println("No generator present. Exiting.");
                    return;
                }

                System.out.println("Total no. of generators : " + countg);

		System.out.println("Select a generator from above points : ");

		int index = Integer.parseInt(in.readLine());

                System.out.println("Scalar multiples of the generator are : ");

                int xCor = xpoint.get(genIndex.get(index));
                int yCor = ypoint.get(genIndex.get(index));
                
                xDouble = xpointDouble(xCor,yCor,g,gi,gj,x,y,genPower);
                yDouble = ypointDouble(xCor,yCor,g,gi,gj,x,y,genPower,xDouble);
                xAdd = xDouble;
                yAdd = yDouble;
                
                System.out.println("1A - (" + genPower[xCor] + "," + genPower[yCor] + ")");
                System.out.println("2A - (" + genPower[xDouble] + "," + genPower[yDouble] + ")");
                //System.out.println("3A - (" + genPower[xAdd] + "," + genPower[yAdd] + ")");

                for(int l=3; l <= countp-1; l++)
                {
                    int temp = xAdd;
                    xAdd = xpointAdd(xCor,yCor, xAdd,yAdd,g ,gi,x ,y ,genPower);
                    yAdd = ypointAdd(xCor,yCor, temp,yAdd,g ,gi,x ,y ,genPower,xAdd);

                    System.out.println((int)(l) + "A - (" + genPower[xAdd] + "," + genPower[yAdd] + ")");
                }

                System.out.println((int)(countp) + "A - Infinity");
	}

        public static int xpointAdd(int x1 , int y1 ,int x2 , int y2 ,
			BigInteger g ,BigInteger gi , int x , int y , BigInteger genPower[]){

                BigInteger temp = genPower[y2].xor(genPower[y1]);
		BigInteger temp1 = genPower[x2].xor(genPower[x1]);

                int num = 0,dem = 0,diff = 0;

                for(int k=0;k<x;k++)
                {   if(temp.equals(genPower[k])) {
                            num = k;
                    }
                    if(temp1.equals(genPower[k])) {
                            dem = k;
                    }
                }

                if(num == x-1)
                {   diff = x-1;
                }else
                {   diff = (num - dem);
                    if (diff<0)
                    {
                        diff = (x - 1 + diff);
                    }
                }

                BigInteger xCor;

                if(diff == x-1)
                {   xCor = genPower[x1].xor(genPower[x2]).xor(genPower[gi.intValue()]);
                }else{
                    BigInteger slope = genPower[diff];
                    BigInteger temp2 = multiply(slope,slope,x,y);
                    xCor = temp2.xor(slope).xor(genPower[x1]).xor(genPower[x2]).xor(genPower[gi.intValue()]);
                }

                for(int k=0;k<x;k++) {
                    if(xCor.equals(genPower[k])) {
                          return k;
                    }
                }
                return -1;
	}

	public static int ypointAdd(int x1 , int y1 ,int x2 , int y2 , BigInteger g ,BigInteger gi ,
                                int x , int y , BigInteger genPower[], int x3){

		BigInteger temp = genPower[y2].xor(genPower[y1]);
		BigInteger temp1 = genPower[x2].xor(genPower[x1]);
		int num = 0,dem = 0,diff = 0;

                for(int k=0;k<x;k++)
                {
                    if(temp.equals(genPower[k])) {
                            num = k;
                    }
                    if(temp1.equals(genPower[k])) {
                            dem = k;
                    }
                }

                if(num == x-1)
                {   diff = x-1;
                }else
                {   diff = (num - dem);
                    if (diff<0)
                    {
                        diff = (x - 1 + diff);
                    }
                }

                BigInteger yCor;

                if(diff == x-1)
                {   yCor = genPower[x3].xor(genPower[y1]);
                }else{
                    BigInteger slope = genPower[diff];
                    BigInteger temp2 = multiply(genPower[x1].xor(genPower[x3]),slope,x,y);
                    yCor = temp2.xor(genPower[x3]).xor(genPower[y1]);
                }

		for(int k=0;k<x;k++) {
                    if(yCor.equals(genPower[k])) {
                          return k;
                    }
                }
                return -1;
        }

        public static int xpointDouble(int xIndex , int yIndex , BigInteger g , BigInteger gi, BigInteger gj ,int x , int y, BigInteger genPower[]){

                int temp=(yIndex-xIndex);

		int negIndex=temp;
		if (temp<0)
		{   negIndex = (x + temp - 1);
		}
		BigInteger slope = (genPower[xIndex].xor(genPower[negIndex]));
		BigInteger sqSlope = multiply(slope,slope,x,y);
		BigInteger xCorDouble =  sqSlope.xor(slope).xor(genPower[gi.intValue()]);

                for(int k=0;k<x;k++) {
                    if(xCorDouble.equals(genPower[k])) {
                          return k;
                    }
                }
                return -1;
	}

         public static int ypointDouble(int xIndex , int yIndex , BigInteger g , BigInteger gi, BigInteger gj ,int x , int y, BigInteger genPower[], int x3){

		int temp=(yIndex-xIndex);
		int negIndex=temp;
		if (temp<0)
		{
			negIndex = (x + temp - 1);
                }

		BigInteger slope = (genPower[xIndex].xor(genPower[negIndex]));
		BigInteger xsq = multiply(genPower[xIndex],genPower[xIndex],x,y);
                BigInteger temp1 = multiply(slope.xor(genPower[0]),genPower[x3],x,y);

		BigInteger yCorDouble = temp1.xor(xsq);

                for(int k=0;k<x;k++) {
                    if(yCorDouble.equals(genPower[k])) {
                          return k;
                    }
                }
                return -1;
    }

	public static boolean curve(BigInteger a, BigInteger b, BigInteger gi,
			BigInteger gj,BigInteger g, Integer x, Integer y, BigInteger genPower[] ) {

            int aIndex = -1,bIndex = -1;
            for(int i=0;i<x;i++) {
                    if(a.equals(genPower[i])) {
                            aIndex = i;
                    }
                    if(b.equals(genPower[i])) {
                            bIndex = i;
                    }
            }

            if(aIndex == x-1)
            {
                if (bIndex == x-1){
                 return false;
                }
                else{
                BigInteger lhs = genPower[new BigInteger((int)(bIndex*2)+"").mod(new BigInteger((x-1)+"")).intValue()];
                return lhs.equals(genPower[gj.intValue()]);
                }
            }else if(bIndex == x-1){
                BigInteger lhs = BigInteger.ZERO;
                BigInteger aCube = genPower[new BigInteger((int)(aIndex*3)+"").mod(new BigInteger((x-1)+"")).intValue()];
                BigInteger giAsquare = genPower[new BigInteger((int)(aIndex*2)+"").add(new BigInteger(gi + "")).mod(new BigInteger((x-1)+"")).intValue()];
                BigInteger rhs = aCube.xor(giAsquare).xor(genPower[gj.intValue()]);
                return lhs.equals(rhs);
            }

            BigInteger bSquare = genPower[new BigInteger((int)(bIndex*2)+"").mod(new BigInteger((x-1)+"")).intValue()];
            BigInteger ab = genPower[new BigInteger((int)(aIndex+bIndex)+"").mod(new BigInteger((x-1)+"")).intValue()];
            BigInteger lhs = bSquare.xor(ab);
            BigInteger aCube = genPower[new BigInteger((int)(aIndex*3)+"").mod(new BigInteger((x-1)+"")).intValue()];
            BigInteger giAsquare = genPower[new BigInteger((int)(aIndex*2)+"").add(new BigInteger(gi + "")).mod(new BigInteger((x-1)+"")).intValue()];
            BigInteger rhs = aCube.xor(giAsquare).xor(genPower[gj.intValue()]);

            return lhs.equals(rhs);
	}
}