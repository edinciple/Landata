package lan.test;

import java.util.Iterator;

import lan.exceptions.IdNotExistException;
import lan.struct.LanInnerObject;
import lan.struct.LanObject;
import lan.struct.LanObjectProcessor;
import lan.struct.LanWorld;

public class CounterObjectProcessor extends LanObjectProcessor implements Runnable {
	
	Thread thread;
	public CounterObjectProcessor(LanWorld owner) {
		super(owner);
		thread = new Thread(this);
	}
	
	@Override
	public Object getRealObject(LanObject object) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LanObject fromRealObject(Object object) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void start() {
		this.thread.start();
	}
	
	@SuppressWarnings("deprecation")
	public void stop() {
		this.thread.stop();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			Iterator<LanObject> iterator;
			try {
				iterator = this.getOwner().findByProcessor(this).iterator();
			} catch (IdNotExistException e) {
				// TODO Auto-generated catch block
				continue;
			}
			
			while(iterator.hasNext()) {
				LanInnerObject entry = (LanInnerObject)iterator.next();
				try {
					entry.setAttribute("count", 
							this.getOwner().fromRealObject("integer", (Integer)entry.getAttribute("count").getRealObject() + 1));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					continue;
				}
			}
		}
	}
}
