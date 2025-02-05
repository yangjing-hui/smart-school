package kxr1.smartcollege.smartcollege.plugin.mob;

import com.kingdee.cosmic.ctrl.kdf.server.IForm;
import com.oracle.truffle.api.object.DynamicObject;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.ext.form.control.MapControl;
import kd.bos.ext.form.control.events.MapSelectEvent;
import kd.bos.ext.form.control.events.MapSelectListener;
import kd.bos.ext.form.dto.MapSelectPointOption;
import kd.bos.form.control.Button;
import kd.bos.form.control.Label;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.operate.FormOperate;
import kd.bos.form.plugin.AbstractMobFormPlugin;
import kd.bos.servicehelper.operation.SaveServiceHelper;
import kd.bos.url.UrlService;

import java.math.BigDecimal;
import java.util.*;

public class location extends AbstractMobFormPlugin implements MapSelectListener {

    @Override
    public void registerListener(EventObject e) {
        super.registerListener(e);
        addClickListeners("kxr1_get_loaction");
        addClickListeners("kxr1_buttonap");
        MapControl mapControl = getControl("kxr1_mapcontrolap");
        mapControl.addSelectListener(this);
    }

    @Override
    public void click(EventObject evt) {
        super.click(evt);
        if (evt.getSource() instanceof Button) {
            switch (((Button) evt.getSource()).getKey()) {
                case "kxr1_get_loaction":
                /*    // 获取当前定位
                    getPageCache().put("selectLocate", "ture");
                    MapControl mapControl = getControl("kxr1_mapcontrolap");
                    mapControl.getAddress();
                    break;*/
                    // 设置地图根据地址标记某个位置*
                    MapControl mapCtl = this.getView().getControl("kxr1_mapcontrolap");
                    mapCtl.selectAddress("江苏省常州市金坛区河海大道1915号河海大学长荡湖校区");
                    break;
                case "kxr1_buttonap":
                    int start = Integer.parseInt(this.getModel().getValue("kxr1_combofield11").toString());
                    int end =Integer.parseInt(this.getModel().getValue("kxr1_combofield1").toString());
                    List<Integer>  shortestPath =Graph.caculate(start,end);
                  //  this.getModel().setValue("kxr1_textfield","shortestPath.toString()");
                  //  this.getView().invokeOperation("save");
                    this.getView().showMessage("从起点到终点的最短路径为:"+shortestPath.toString());
                    break;
            }
        }
    }

    @Override
    public void select(MapSelectEvent evt) {
        Map<String, Object> map = evt.getPoint();

        // 地图控件的 selectAddress,getAddress方法都会调用这个回调，为防止死循环，需要做一个标志位判断
        if ("ture".equals(getPageCache().get("selectLocate"))) {
            getPageCache().remove("selectLocate");
            Map point = (Map) map.get("point");
            // evt中获取控件
            MapControl mapControl = (MapControl) evt.getSource();
            MapSelectPointOption mapSelectPointOption = new MapSelectPointOption();
            // 经度
            mapSelectPointOption.setLng(((BigDecimal) point.get("lng")).doubleValue());
            // 维度
            mapSelectPointOption.setLat(((BigDecimal) point.get("lat")).doubleValue());
            mapControl.selectPoint(mapSelectPointOption);
        }
       //   getModel().setValue("kxr1_loaction", map.get("address"));

    }

    public static class Graph {
        private final Map<Integer, Map<Integer, Integer>> adjacencyList;

        public Graph() {
            adjacencyList = new HashMap<>();
        }

        public void addEdge(int source, int destination, int weight) {
            adjacencyList.computeIfAbsent(source, k -> new HashMap<>()).put(destination, weight);
            adjacencyList.computeIfAbsent(destination, k -> new HashMap<>()).put(source, weight); // for undirected graph
        }

        public List<Integer> shortestPath(int startVertex, int endVertex) {
            Map<Integer, Integer> distances = new HashMap<>();
            Map<Integer, Integer> previousVertices = new HashMap<>();
            PriorityQueue<Integer> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(distances::get));

            distances.put(startVertex, 0);
            priorityQueue.add(startVertex);

            while (!priorityQueue.isEmpty()) {
                int currentVertex = priorityQueue.poll();

                if (currentVertex == endVertex) {
                    break; // Found shortest path to end vertex
                }

                if (adjacencyList.containsKey(currentVertex)) {
                    for (Map.Entry<Integer, Integer> neighborEntry : adjacencyList.get(currentVertex).entrySet()) {
                        int neighbor = neighborEntry.getKey();
                        int weight = neighborEntry.getValue();
                        int distance = distances.getOrDefault(currentVertex, Integer.MAX_VALUE) + weight;

                        if (distance < distances.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                            distances.put(neighbor, distance);
                            previousVertices.put(neighbor, currentVertex);
                            priorityQueue.add(neighbor);
                        }
                    }
                }
            }

            // Reconstruct the shortest path
            List<Integer> path = new ArrayList<>();
            Integer current = endVertex;
            while (current != null) {
                path.add(current);
                current = previousVertices.get(current);
            }
            Collections.reverse(path);

            return path;
        }

        public static List<Integer> caculate(int start, int end) {
            Graph graph = new Graph();
            graph.addEdge(13, 14, 61);
            graph.addEdge(14, 13, 61);
            graph.addEdge(14, 19, 44);
            graph.addEdge(19, 14, 44);
            graph.addEdge(15, 19, 46);
            graph.addEdge(19, 15, 46);

            graph.addEdge(15, 16, 32);
            graph.addEdge(16, 15, 32);
            graph.addEdge(15, 17, 66);
            graph.addEdge(17, 15, 66);
            graph.addEdge(16, 17, 56);
            graph.addEdge(17, 16, 56);
            graph.addEdge(12, 13, 61);
            graph.addEdge(13, 12, 61);
            graph.addEdge(11, 12, 39);
            graph.addEdge(12, 11, 39);
            graph.addEdge(17, 18, 32);
            graph.addEdge(18, 17, 32);
            graph.addEdge(11, 20, 109);
            graph.addEdge(20, 11, 109);
            graph.addEdge(10, 11, 183);
            graph.addEdge(11, 10, 183);
            graph.addEdge(9, 10, 125);
            graph.addEdge(10, 9, 125);
            graph.addEdge(8, 10, 99);
            graph.addEdge(10, 8, 99);
            graph.addEdge(7, 9, 245);
            graph.addEdge(9, 7, 245);
            graph.addEdge(7, 8, 242);
            graph.addEdge(8, 7, 242);
            graph.addEdge(7, 6, 242);
            graph.addEdge(6, 7, 242);
            graph.addEdge(5, 8, 96);
            graph.addEdge(8,5, 96);
            graph.addEdge(4, 5, 83);
            graph.addEdge(5, 4, 83);
            graph.addEdge(3, 4, 62);
            graph.addEdge(4, 3, 62);
            graph.addEdge(4, 58, 89);
            graph.addEdge(58, 4, 89);
            graph.addEdge(3, 58, 97);
            graph.addEdge(58, 3, 97);
            graph.addEdge(3, 56, 98);
            graph.addEdge(56, 3, 98);
            graph.addEdge(56, 58, 109);
            graph.addEdge(58, 56, 109);
            graph.addEdge(57, 58, 93);
            graph.addEdge(58, 57, 93);
            graph.addEdge(58, 59, 112);
            graph.addEdge(57, 59, 87);
            graph.addEdge(59, 57, 87);
            graph.addEdge(55, 56, 71);
            graph.addEdge(56, 55, 71);
            graph.addEdge(55, 57, 101);
            graph.addEdge(57, 55, 101);
            graph.addEdge(55, 46, 86);
            graph.addEdge(46, 55, 86);
            graph.addEdge(44, 46, 44);
            graph.addEdge(46, 44, 44);
            graph.addEdge(46, 47, 34);
            graph.addEdge(47, 46, 34);
            graph.addEdge(44, 47, 23);
            graph.addEdge(47, 44, 23);
            graph.addEdge(47, 48, 41);
            graph.addEdge(48, 47, 41);
            graph.addEdge(48, 54, 41);
            graph.addEdge(54, 48, 41);
            graph.addEdge(49, 54, 32);
            graph.addEdge(54, 49, 32);
            graph.addEdge(49, 50, 45);
            graph.addEdge(50, 49, 45);
            graph.addEdge(50, 51, 48);
            graph.addEdge(51, 50, 48);
            graph.addEdge(51, 52, 48);
            graph.addEdge(52, 51, 48);
            graph.addEdge(52, 53, 48);
            graph.addEdge(53, 52, 48);
            graph.addEdge(52, 59, 92);
            graph.addEdge(59, 52, 92);
            graph.addEdge(57, 49, 142);
            graph.addEdge(49, 57, 142);
            graph.addEdge(43, 45, 94);
            graph.addEdge(45, 43, 94);
            graph.addEdge(42, 43, 138);
            graph.addEdge(43, 42, 138);
            graph.addEdge(42, 45, 188);
            graph.addEdge(45, 42, 188);
            graph.addEdge(35, 42, 132);
            graph.addEdge(42, 35, 132);
            graph.addEdge(41, 42, 41);
            graph.addEdge(42, 41, 41);
            graph.addEdge(40, 41, 32);
            graph.addEdge(41, 40, 32);
            graph.addEdge(38, 40, 60);
            graph.addEdge(40, 38, 60);
            graph.addEdge(39, 41, 60);
            graph.addEdge(41, 39, 60);
            graph.addEdge(36, 38, 60);
            graph.addEdge(38, 36, 60);
            graph.addEdge(37, 39, 60);
            graph.addEdge(39, 37, 60);
            graph.addEdge(38, 39, 32);
            graph.addEdge(39, 38, 32);
            graph.addEdge(36, 37, 32);
            graph.addEdge(37, 36, 32);
            graph.addEdge(35, 37, 56);
            graph.addEdge(37, 35, 56);
            graph.addEdge(34, 35, 77);
            graph.addEdge(35, 34, 77);
            graph.addEdge(37, 34, 77);
            graph.addEdge(34, 37, 77);
            graph.addEdge(33, 34, 102);
            graph.addEdge(34, 33, 102);
            graph.addEdge(32, 33, 77);
            graph.addEdge(33, 32, 77);
            graph.addEdge(22, 32, 225);
            graph.addEdge(32, 22, 225);
            graph.addEdge(22, 23, 81);
            graph.addEdge(23, 22, 81);
            graph.addEdge(21, 22, 84);
            graph.addEdge(22, 21, 84);
            graph.addEdge(20, 23, 151);
            graph.addEdge(23, 20, 151);
            graph.addEdge(20, 21, 102);
            graph.addEdge(21, 20, 102);
            graph.addEdge(17, 23, 81);
            graph.addEdge(23, 17, 81);
            graph.addEdge(1, 22, 257);
            graph.addEdge(22, 1, 257);
            graph.addEdge(1, 2, 70);
            graph.addEdge(2, 1, 70);
            graph.addEdge(2, 33, 137);
            graph.addEdge(33, 2, 137);
            graph.addEdge(34, 2, 122);
            graph.addEdge(2, 34, 122);
            graph.addEdge(35, 56, 225);
            graph.addEdge(56, 35, 225);
            graph.addEdge(1, 3, 221);
            graph.addEdge(3, 1, 221);
            graph.addEdge(7, 1, 316);
            graph.addEdge(1, 7, 316);
            graph.addEdge(6, 1, 374);
            graph.addEdge(1, 6, 374);
            graph.addEdge(32, 28, 202);
            graph.addEdge(28, 32, 202);
            graph.addEdge(18, 25, 96);
            graph.addEdge(25, 18, 96);
            graph.addEdge(31, 25, 55);
            graph.addEdge(25, 31, 55);
            graph.addEdge(26, 25, 21);
            graph.addEdge(25, 26, 21);
            graph.addEdge(26, 27, 19);
            graph.addEdge(27, 26, 19);
            graph.addEdge(28, 27, 17);
            graph.addEdge(27, 28, 17);
            graph.addEdge(28, 29, 11);
            graph.addEdge(29, 28, 11);
            graph.addEdge(29, 30, 27);
            graph.addEdge(30, 29, 27);
            graph.addEdge(30, 31, 86);
            graph.addEdge(31, 30, 86);

            int startVertex = start;
            int endVertex = end;
            List<Integer> shortestPath = graph.shortestPath(startVertex, endVertex);
            return shortestPath;
        //    System.out.println("Shortest path from " + startVertex + " to " + endVertex + ": " + shortestPath);
        }
    }

  /*  @Override
    public void beforeDoOperation(BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        String opGpt = ((FormOperate) args.getSource()).getOperateKey();
        if (StringUtils.equals("getroud", opGpt)) {
            int start = (int) this.getModel().getValue("kxr1_combofield11");
            int end =(int)this.getModel().getValue("kxr1_combofield1");
            List<Integer>  shortestPath =Graph.caculate(start,end);
            this.getModel().setValue("kxr1_textfield","shortestPath.toString()");
            Label label = this.getView().getControl("kxr1_labelap");
            label.setText(shortestPath.toString());
          //  SaveServiceHelper.update();
            //  this.getView().invokeOperation("save");
            //  this.getView().showMessage("shortestPath.toString()");
          //  System.out.println(shortestPath);
            String domain = UrlService.getDomainContextUrl()+"/index.html";
            this.getView().openUrl(domain+"?formId="+"kxr1_cselectionlist"+"&accountId="+ RequestContext.get().getAccountId());

        }
    }*/
}
