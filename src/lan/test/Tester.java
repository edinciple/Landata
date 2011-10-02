package lan.test;

import lan.struct.*;
import lan.struct.mongodb.*;

public class Tester {
	public static void main(String[] args) {
		try {
			MongodbLanObjectProvider provider = new MongodbLanObjectProvider();
			LanWorld world = new LanWorld(provider);
			LanInnerObject innerObj = world.createInnerObject();
			LanOuterObject msgStrObj = world.createOuterObject();
			msgStrObj.setProcessorString("string");
			msgStrObj.setMetadata("Hello, Landata!\nHello, Mongodb!");
			innerObj.setAttribute("msg", msgStrObj);
			LanOuterObject msgObj = (LanOuterObject)innerObj.getAttribute("msg");
			System.out.print(msgObj.getMetadata());
		} catch (Exception e) {
			System.out.print(e.toString());
		}
	}
}
