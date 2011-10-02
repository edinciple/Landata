package lan.test;

import lan.struct.*;

public class Tester {
	public static void main(String[] args) {
		try {
			LanWorld world = new LanWorld();
			LanInnerObject innerObj = world.createInnerObject();
			LanOuterObject msgStrObj = world.createOuterObject();
			msgStrObj.setProcessorName("string");
			msgStrObj.setMetadata("Hello, Landata!\nHello, Mongodb!");
			innerObj.setAttribute("msg", msgStrObj);
			LanOuterObject msgObj = (LanOuterObject)innerObj.getAttribute("msg");
			System.out.print(msgObj.getMetadata());
		} catch (Exception e) {
			System.out.print(e.toString());
		}
	}
}
