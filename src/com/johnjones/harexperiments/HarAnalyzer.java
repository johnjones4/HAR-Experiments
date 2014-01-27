package com.johnjones.harexperiments;

import java.io.File;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.file.FileSinkImages;
import org.graphstream.stream.file.FileSinkImages.LayoutPolicy;
import org.graphstream.stream.file.FileSinkImages.OutputType;
import org.graphstream.stream.file.FileSinkImages.Resolutions;

import com.johnjones.harexperiments.model.Har;
import com.johnjones.harexperiments.model.Resource;

public class HarAnalyzer {

	public static void main(String[] args) {
		if (args.length == 1) {
			try {
				Har har = Har.parseFile(new File(args[0]));
				Resource root = har.computeRequestTree();
				Graph graph = new SingleGraph(root.getUrl().toString());
				graph.addAttribute("ui.stylesheet","node { text-size: 5px; }");
				graph.display();
				Node rootNode = graph.addNode(root.getResourceID());
				buildGraph(graph,rootNode,root);
				
				FileSinkImages pic = new FileSinkImages(OutputType.PNG, Resolutions.QSXGA);
				pic.setLayoutPolicy(LayoutPolicy.COMPUTED_FULLY_AT_NEW_IMAGE);
				pic.writeAll(graph, "/Users/johnjones/Desktop/test1.png");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			usage();
		}
	}
	
	private static void buildGraph(Graph graph,Node node,Resource r) {
		//node.setAttribute("ui.label", r.getUrl().toString());
		try {
			Thread.sleep((long)r.getLoadTime() / 10);
		} catch (InterruptedException e) {}
		if (r.getChildren() != null) {
			for(Resource child : r.getChildren()) {
				Node childNode = graph.addNode(child.getResourceID());
				graph.addEdge(r.getResourceID()+"-"+child.getResourceID(), node, childNode, true);
				buildGraph(graph,childNode,child);
			}
		}
	}
	
	private static void usage() {
		System.out.println("Need file to parse");
		System.exit(1);
	}

}
