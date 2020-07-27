/*
 * Copyright 2015-2016 Alessio Vinerbi. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anchorage.docks.stations;


import com.anchorage.docks.node.DockNode;
import com.anchorage.docks.node.ui.DockUIPanel;
import javafx.scene.Scene;
import javafx.stage.Window;

/**
 *
 * @author avinerbi
 */
public final class DockSubStation extends DockNode {
    
     private final DockStation substation;
    /**
     * Get the value of station
     *
     * @return the value of station
     */
    
    public DockStation getSubStation() {
        return substation;
    }

    public Window getStationWindow()
    {
        return stationProperty().get().getStationScene().getWindow();
    }
    
    public Scene getStationScene()
    {
        return stationProperty().get().getStationScene();
    }
    
    public void putDock(DockNode dockNode, DockPosition position, double percentage)  {
        substation.add(dockNode);
        substation.putDock(dockNode, position,percentage);
        dockNode.stationProperty().set(substation);
    }
     
      
    public DockSubStation(DockUIPanel uiPanel) {
        super(uiPanel);
        substation = (DockStation)getContent().getNodeContent();
        substation.markAsSubStation(this);
    }
    
}
