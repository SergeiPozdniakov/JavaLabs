/*
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
$(document).ready(function() {

    $(".click-title").mouseenter( function(    e){
        e.preventDefault();
        this.style.cursor="pointer";
    });
    $(".click-title").mousedown( function(event){
        event.preventDefault();
    });

    // Ugly code while this script is shared among several pages
    try{
        refreshHitsPerSecond(true);
    } catch(e){}
    try{
        refreshResponseTimeOverTime(true);
    } catch(e){}
    try{
        refreshResponseTimePercentiles();
    } catch(e){}
});


var responseTimePercentilesInfos = {
        data: {"result": {"minY": 113.0, "minX": 0.0, "maxY": 1261.0, "series": [{"data": [[0.0, 113.0], [0.1, 122.0], [0.2, 124.0], [0.3, 126.0], [0.4, 128.0], [0.5, 129.0], [0.6, 130.0], [0.7, 131.0], [0.8, 132.0], [0.9, 132.0], [1.0, 133.0], [1.1, 134.0], [1.2, 135.0], [1.3, 135.0], [1.4, 136.0], [1.5, 137.0], [1.6, 138.0], [1.7, 138.0], [1.8, 139.0], [1.9, 140.0], [2.0, 140.0], [2.1, 141.0], [2.2, 141.0], [2.3, 142.0], [2.4, 143.0], [2.5, 143.0], [2.6, 144.0], [2.7, 144.0], [2.8, 144.0], [2.9, 145.0], [3.0, 145.0], [3.1, 146.0], [3.2, 146.0], [3.3, 146.0], [3.4, 147.0], [3.5, 147.0], [3.6, 148.0], [3.7, 148.0], [3.8, 148.0], [3.9, 149.0], [4.0, 149.0], [4.1, 149.0], [4.2, 150.0], [4.3, 150.0], [4.4, 150.0], [4.5, 150.0], [4.6, 151.0], [4.7, 151.0], [4.8, 151.0], [4.9, 152.0], [5.0, 152.0], [5.1, 152.0], [5.2, 152.0], [5.3, 153.0], [5.4, 153.0], [5.5, 153.0], [5.6, 154.0], [5.7, 154.0], [5.8, 154.0], [5.9, 154.0], [6.0, 155.0], [6.1, 155.0], [6.2, 155.0], [6.3, 155.0], [6.4, 155.0], [6.5, 156.0], [6.6, 156.0], [6.7, 156.0], [6.8, 156.0], [6.9, 156.0], [7.0, 157.0], [7.1, 157.0], [7.2, 157.0], [7.3, 157.0], [7.4, 157.0], [7.5, 157.0], [7.6, 158.0], [7.7, 158.0], [7.8, 158.0], [7.9, 158.0], [8.0, 158.0], [8.1, 158.0], [8.2, 159.0], [8.3, 159.0], [8.4, 159.0], [8.5, 159.0], [8.6, 159.0], [8.7, 160.0], [8.8, 160.0], [8.9, 160.0], [9.0, 160.0], [9.1, 160.0], [9.2, 160.0], [9.3, 160.0], [9.4, 161.0], [9.5, 161.0], [9.6, 161.0], [9.7, 161.0], [9.8, 161.0], [9.9, 161.0], [10.0, 161.0], [10.1, 162.0], [10.2, 162.0], [10.3, 162.0], [10.4, 162.0], [10.5, 162.0], [10.6, 162.0], [10.7, 163.0], [10.8, 163.0], [10.9, 163.0], [11.0, 163.0], [11.1, 163.0], [11.2, 163.0], [11.3, 163.0], [11.4, 164.0], [11.5, 164.0], [11.6, 164.0], [11.7, 164.0], [11.8, 164.0], [11.9, 164.0], [12.0, 164.0], [12.1, 165.0], [12.2, 165.0], [12.3, 165.0], [12.4, 165.0], [12.5, 165.0], [12.6, 165.0], [12.7, 165.0], [12.8, 166.0], [12.9, 166.0], [13.0, 166.0], [13.1, 166.0], [13.2, 166.0], [13.3, 166.0], [13.4, 166.0], [13.5, 166.0], [13.6, 167.0], [13.7, 167.0], [13.8, 167.0], [13.9, 167.0], [14.0, 167.0], [14.1, 167.0], [14.2, 167.0], [14.3, 168.0], [14.4, 168.0], [14.5, 168.0], [14.6, 168.0], [14.7, 168.0], [14.8, 168.0], [14.9, 168.0], [15.0, 169.0], [15.1, 169.0], [15.2, 169.0], [15.3, 169.0], [15.4, 169.0], [15.5, 169.0], [15.6, 169.0], [15.7, 169.0], [15.8, 169.0], [15.9, 170.0], [16.0, 170.0], [16.1, 170.0], [16.2, 170.0], [16.3, 170.0], [16.4, 170.0], [16.5, 170.0], [16.6, 170.0], [16.7, 171.0], [16.8, 171.0], [16.9, 171.0], [17.0, 171.0], [17.1, 171.0], [17.2, 171.0], [17.3, 171.0], [17.4, 171.0], [17.5, 172.0], [17.6, 172.0], [17.7, 172.0], [17.8, 172.0], [17.9, 172.0], [18.0, 172.0], [18.1, 172.0], [18.2, 172.0], [18.3, 172.0], [18.4, 173.0], [18.5, 173.0], [18.6, 173.0], [18.7, 173.0], [18.8, 173.0], [18.9, 173.0], [19.0, 173.0], [19.1, 173.0], [19.2, 174.0], [19.3, 174.0], [19.4, 174.0], [19.5, 174.0], [19.6, 174.0], [19.7, 174.0], [19.8, 174.0], [19.9, 174.0], [20.0, 174.0], [20.1, 174.0], [20.2, 175.0], [20.3, 175.0], [20.4, 175.0], [20.5, 175.0], [20.6, 175.0], [20.7, 175.0], [20.8, 175.0], [20.9, 175.0], [21.0, 175.0], [21.1, 176.0], [21.2, 176.0], [21.3, 176.0], [21.4, 176.0], [21.5, 176.0], [21.6, 176.0], [21.7, 176.0], [21.8, 176.0], [21.9, 176.0], [22.0, 176.0], [22.1, 177.0], [22.2, 177.0], [22.3, 177.0], [22.4, 177.0], [22.5, 177.0], [22.6, 177.0], [22.7, 177.0], [22.8, 177.0], [22.9, 177.0], [23.0, 178.0], [23.1, 178.0], [23.2, 178.0], [23.3, 178.0], [23.4, 178.0], [23.5, 178.0], [23.6, 178.0], [23.7, 178.0], [23.8, 178.0], [23.9, 178.0], [24.0, 178.0], [24.1, 179.0], [24.2, 179.0], [24.3, 179.0], [24.4, 179.0], [24.5, 179.0], [24.6, 179.0], [24.7, 179.0], [24.8, 179.0], [24.9, 179.0], [25.0, 179.0], [25.1, 180.0], [25.2, 180.0], [25.3, 180.0], [25.4, 180.0], [25.5, 180.0], [25.6, 180.0], [25.7, 180.0], [25.8, 180.0], [25.9, 180.0], [26.0, 180.0], [26.1, 180.0], [26.2, 181.0], [26.3, 181.0], [26.4, 181.0], [26.5, 181.0], [26.6, 181.0], [26.7, 181.0], [26.8, 181.0], [26.9, 181.0], [27.0, 181.0], [27.1, 181.0], [27.2, 182.0], [27.3, 182.0], [27.4, 182.0], [27.5, 182.0], [27.6, 182.0], [27.7, 182.0], [27.8, 182.0], [27.9, 182.0], [28.0, 182.0], [28.1, 182.0], [28.2, 182.0], [28.3, 183.0], [28.4, 183.0], [28.5, 183.0], [28.6, 183.0], [28.7, 183.0], [28.8, 183.0], [28.9, 183.0], [29.0, 183.0], [29.1, 183.0], [29.2, 183.0], [29.3, 183.0], [29.4, 184.0], [29.5, 184.0], [29.6, 184.0], [29.7, 184.0], [29.8, 184.0], [29.9, 184.0], [30.0, 184.0], [30.1, 184.0], [30.2, 184.0], [30.3, 184.0], [30.4, 184.0], [30.5, 184.0], [30.6, 185.0], [30.7, 185.0], [30.8, 185.0], [30.9, 185.0], [31.0, 185.0], [31.1, 185.0], [31.2, 185.0], [31.3, 185.0], [31.4, 185.0], [31.5, 185.0], [31.6, 185.0], [31.7, 186.0], [31.8, 186.0], [31.9, 186.0], [32.0, 186.0], [32.1, 186.0], [32.2, 186.0], [32.3, 186.0], [32.4, 186.0], [32.5, 186.0], [32.6, 186.0], [32.7, 186.0], [32.8, 186.0], [32.9, 186.0], [33.0, 187.0], [33.1, 187.0], [33.2, 187.0], [33.3, 187.0], [33.4, 187.0], [33.5, 187.0], [33.6, 187.0], [33.7, 187.0], [33.8, 187.0], [33.9, 187.0], [34.0, 187.0], [34.1, 187.0], [34.2, 188.0], [34.3, 188.0], [34.4, 188.0], [34.5, 188.0], [34.6, 188.0], [34.7, 188.0], [34.8, 188.0], [34.9, 188.0], [35.0, 188.0], [35.1, 188.0], [35.2, 188.0], [35.3, 188.0], [35.4, 189.0], [35.5, 189.0], [35.6, 189.0], [35.7, 189.0], [35.8, 189.0], [35.9, 189.0], [36.0, 189.0], [36.1, 189.0], [36.2, 189.0], [36.3, 189.0], [36.4, 189.0], [36.5, 189.0], [36.6, 190.0], [36.7, 190.0], [36.8, 190.0], [36.9, 190.0], [37.0, 190.0], [37.1, 190.0], [37.2, 190.0], [37.3, 190.0], [37.4, 190.0], [37.5, 190.0], [37.6, 190.0], [37.7, 190.0], [37.8, 191.0], [37.9, 191.0], [38.0, 191.0], [38.1, 191.0], [38.2, 191.0], [38.3, 191.0], [38.4, 191.0], [38.5, 191.0], [38.6, 191.0], [38.7, 191.0], [38.8, 191.0], [38.9, 191.0], [39.0, 192.0], [39.1, 192.0], [39.2, 192.0], [39.3, 192.0], [39.4, 192.0], [39.5, 192.0], [39.6, 192.0], [39.7, 192.0], [39.8, 192.0], [39.9, 192.0], [40.0, 192.0], [40.1, 192.0], [40.2, 193.0], [40.3, 193.0], [40.4, 193.0], [40.5, 193.0], [40.6, 193.0], [40.7, 193.0], [40.8, 193.0], [40.9, 193.0], [41.0, 193.0], [41.1, 193.0], [41.2, 193.0], [41.3, 193.0], [41.4, 194.0], [41.5, 194.0], [41.6, 194.0], [41.7, 194.0], [41.8, 194.0], [41.9, 194.0], [42.0, 194.0], [42.1, 194.0], [42.2, 194.0], [42.3, 194.0], [42.4, 194.0], [42.5, 194.0], [42.6, 195.0], [42.7, 195.0], [42.8, 195.0], [42.9, 195.0], [43.0, 195.0], [43.1, 195.0], [43.2, 195.0], [43.3, 195.0], [43.4, 195.0], [43.5, 195.0], [43.6, 195.0], [43.7, 195.0], [43.8, 195.0], [43.9, 196.0], [44.0, 196.0], [44.1, 196.0], [44.2, 196.0], [44.3, 196.0], [44.4, 196.0], [44.5, 196.0], [44.6, 196.0], [44.7, 196.0], [44.8, 196.0], [44.9, 196.0], [45.0, 197.0], [45.1, 197.0], [45.2, 197.0], [45.3, 197.0], [45.4, 197.0], [45.5, 197.0], [45.6, 197.0], [45.7, 197.0], [45.8, 197.0], [45.9, 197.0], [46.0, 197.0], [46.1, 197.0], [46.2, 198.0], [46.3, 198.0], [46.4, 198.0], [46.5, 198.0], [46.6, 198.0], [46.7, 198.0], [46.8, 198.0], [46.9, 198.0], [47.0, 198.0], [47.1, 198.0], [47.2, 198.0], [47.3, 198.0], [47.4, 198.0], [47.5, 199.0], [47.6, 199.0], [47.7, 199.0], [47.8, 199.0], [47.9, 199.0], [48.0, 199.0], [48.1, 199.0], [48.2, 199.0], [48.3, 199.0], [48.4, 199.0], [48.5, 199.0], [48.6, 199.0], [48.7, 200.0], [48.8, 200.0], [48.9, 200.0], [49.0, 200.0], [49.1, 200.0], [49.2, 200.0], [49.3, 200.0], [49.4, 200.0], [49.5, 200.0], [49.6, 200.0], [49.7, 200.0], [49.8, 201.0], [49.9, 201.0], [50.0, 201.0], [50.1, 201.0], [50.2, 201.0], [50.3, 201.0], [50.4, 201.0], [50.5, 201.0], [50.6, 201.0], [50.7, 201.0], [50.8, 201.0], [50.9, 202.0], [51.0, 202.0], [51.1, 202.0], [51.2, 202.0], [51.3, 202.0], [51.4, 202.0], [51.5, 202.0], [51.6, 202.0], [51.7, 202.0], [51.8, 202.0], [51.9, 202.0], [52.0, 202.0], [52.1, 203.0], [52.2, 203.0], [52.3, 203.0], [52.4, 203.0], [52.5, 203.0], [52.6, 203.0], [52.7, 203.0], [52.8, 203.0], [52.9, 203.0], [53.0, 203.0], [53.1, 203.0], [53.2, 204.0], [53.3, 204.0], [53.4, 204.0], [53.5, 204.0], [53.6, 204.0], [53.7, 204.0], [53.8, 204.0], [53.9, 204.0], [54.0, 204.0], [54.1, 204.0], [54.2, 204.0], [54.3, 205.0], [54.4, 205.0], [54.5, 205.0], [54.6, 205.0], [54.7, 205.0], [54.8, 205.0], [54.9, 205.0], [55.0, 205.0], [55.1, 205.0], [55.2, 205.0], [55.3, 205.0], [55.4, 205.0], [55.5, 206.0], [55.6, 206.0], [55.7, 206.0], [55.8, 206.0], [55.9, 206.0], [56.0, 206.0], [56.1, 206.0], [56.2, 206.0], [56.3, 206.0], [56.4, 206.0], [56.5, 206.0], [56.6, 207.0], [56.7, 207.0], [56.8, 207.0], [56.9, 207.0], [57.0, 207.0], [57.1, 207.0], [57.2, 207.0], [57.3, 207.0], [57.4, 207.0], [57.5, 207.0], [57.6, 207.0], [57.7, 208.0], [57.8, 208.0], [57.9, 208.0], [58.0, 208.0], [58.1, 208.0], [58.2, 208.0], [58.3, 208.0], [58.4, 208.0], [58.5, 208.0], [58.6, 208.0], [58.7, 208.0], [58.8, 209.0], [58.9, 209.0], [59.0, 209.0], [59.1, 209.0], [59.2, 209.0], [59.3, 209.0], [59.4, 209.0], [59.5, 209.0], [59.6, 209.0], [59.7, 209.0], [59.8, 209.0], [59.9, 209.0], [60.0, 210.0], [60.1, 210.0], [60.2, 210.0], [60.3, 210.0], [60.4, 210.0], [60.5, 210.0], [60.6, 210.0], [60.7, 210.0], [60.8, 210.0], [60.9, 210.0], [61.0, 211.0], [61.1, 211.0], [61.2, 211.0], [61.3, 211.0], [61.4, 211.0], [61.5, 211.0], [61.6, 211.0], [61.7, 211.0], [61.8, 211.0], [61.9, 211.0], [62.0, 211.0], [62.1, 212.0], [62.2, 212.0], [62.3, 212.0], [62.4, 212.0], [62.5, 212.0], [62.6, 212.0], [62.7, 212.0], [62.8, 212.0], [62.9, 212.0], [63.0, 212.0], [63.1, 213.0], [63.2, 213.0], [63.3, 213.0], [63.4, 213.0], [63.5, 213.0], [63.6, 213.0], [63.7, 213.0], [63.8, 213.0], [63.9, 213.0], [64.0, 213.0], [64.1, 214.0], [64.2, 214.0], [64.3, 214.0], [64.4, 214.0], [64.5, 214.0], [64.6, 214.0], [64.7, 214.0], [64.8, 214.0], [64.9, 214.0], [65.0, 214.0], [65.1, 215.0], [65.2, 215.0], [65.3, 215.0], [65.4, 215.0], [65.5, 215.0], [65.6, 215.0], [65.7, 215.0], [65.8, 215.0], [65.9, 215.0], [66.0, 215.0], [66.1, 216.0], [66.2, 216.0], [66.3, 216.0], [66.4, 216.0], [66.5, 216.0], [66.6, 216.0], [66.7, 216.0], [66.8, 216.0], [66.9, 216.0], [67.0, 216.0], [67.1, 217.0], [67.2, 217.0], [67.3, 217.0], [67.4, 217.0], [67.5, 217.0], [67.6, 217.0], [67.7, 217.0], [67.8, 217.0], [67.9, 217.0], [68.0, 217.0], [68.1, 218.0], [68.2, 218.0], [68.3, 218.0], [68.4, 218.0], [68.5, 218.0], [68.6, 218.0], [68.7, 218.0], [68.8, 218.0], [68.9, 218.0], [69.0, 218.0], [69.1, 219.0], [69.2, 219.0], [69.3, 219.0], [69.4, 219.0], [69.5, 219.0], [69.6, 219.0], [69.7, 219.0], [69.8, 219.0], [69.9, 219.0], [70.0, 219.0], [70.1, 220.0], [70.2, 220.0], [70.3, 220.0], [70.4, 220.0], [70.5, 220.0], [70.6, 220.0], [70.7, 220.0], [70.8, 220.0], [70.9, 220.0], [71.0, 220.0], [71.1, 221.0], [71.2, 221.0], [71.3, 221.0], [71.4, 221.0], [71.5, 221.0], [71.6, 221.0], [71.7, 221.0], [71.8, 221.0], [71.9, 221.0], [72.0, 222.0], [72.1, 222.0], [72.2, 222.0], [72.3, 222.0], [72.4, 222.0], [72.5, 222.0], [72.6, 222.0], [72.7, 222.0], [72.8, 222.0], [72.9, 223.0], [73.0, 223.0], [73.1, 223.0], [73.2, 223.0], [73.3, 223.0], [73.4, 223.0], [73.5, 223.0], [73.6, 223.0], [73.7, 223.0], [73.8, 223.0], [73.9, 223.0], [74.0, 224.0], [74.1, 224.0], [74.2, 224.0], [74.3, 224.0], [74.4, 224.0], [74.5, 224.0], [74.6, 224.0], [74.7, 224.0], [74.8, 224.0], [74.9, 225.0], [75.0, 225.0], [75.1, 225.0], [75.2, 225.0], [75.3, 225.0], [75.4, 225.0], [75.5, 225.0], [75.6, 225.0], [75.7, 225.0], [75.8, 225.0], [75.9, 226.0], [76.0, 226.0], [76.1, 226.0], [76.2, 226.0], [76.3, 226.0], [76.4, 226.0], [76.5, 226.0], [76.6, 226.0], [76.7, 226.0], [76.8, 227.0], [76.9, 227.0], [77.0, 227.0], [77.1, 227.0], [77.2, 227.0], [77.3, 227.0], [77.4, 227.0], [77.5, 227.0], [77.6, 227.0], [77.7, 228.0], [77.8, 228.0], [77.9, 228.0], [78.0, 228.0], [78.1, 228.0], [78.2, 228.0], [78.3, 228.0], [78.4, 228.0], [78.5, 228.0], [78.6, 229.0], [78.7, 229.0], [78.8, 229.0], [78.9, 229.0], [79.0, 229.0], [79.1, 229.0], [79.2, 229.0], [79.3, 229.0], [79.4, 230.0], [79.5, 230.0], [79.6, 230.0], [79.7, 230.0], [79.8, 230.0], [79.9, 230.0], [80.0, 230.0], [80.1, 231.0], [80.2, 231.0], [80.3, 231.0], [80.4, 231.0], [80.5, 231.0], [80.6, 231.0], [80.7, 231.0], [80.8, 231.0], [80.9, 231.0], [81.0, 232.0], [81.1, 232.0], [81.2, 232.0], [81.3, 232.0], [81.4, 232.0], [81.5, 232.0], [81.6, 232.0], [81.7, 233.0], [81.8, 233.0], [81.9, 233.0], [82.0, 233.0], [82.1, 233.0], [82.2, 233.0], [82.3, 233.0], [82.4, 234.0], [82.5, 234.0], [82.6, 234.0], [82.7, 234.0], [82.8, 234.0], [82.9, 234.0], [83.0, 234.0], [83.1, 234.0], [83.2, 235.0], [83.3, 235.0], [83.4, 235.0], [83.5, 235.0], [83.6, 235.0], [83.7, 235.0], [83.8, 235.0], [83.9, 236.0], [84.0, 236.0], [84.1, 236.0], [84.2, 236.0], [84.3, 236.0], [84.4, 236.0], [84.5, 237.0], [84.6, 237.0], [84.7, 237.0], [84.8, 237.0], [84.9, 237.0], [85.0, 237.0], [85.1, 238.0], [85.2, 238.0], [85.3, 238.0], [85.4, 238.0], [85.5, 238.0], [85.6, 238.0], [85.7, 239.0], [85.8, 239.0], [85.9, 239.0], [86.0, 239.0], [86.1, 239.0], [86.2, 239.0], [86.3, 240.0], [86.4, 240.0], [86.5, 240.0], [86.6, 240.0], [86.7, 240.0], [86.8, 241.0], [86.9, 241.0], [87.0, 241.0], [87.1, 241.0], [87.2, 241.0], [87.3, 241.0], [87.4, 242.0], [87.5, 242.0], [87.6, 242.0], [87.7, 242.0], [87.8, 242.0], [87.9, 242.0], [88.0, 243.0], [88.1, 243.0], [88.2, 243.0], [88.3, 243.0], [88.4, 243.0], [88.5, 244.0], [88.6, 244.0], [88.7, 244.0], [88.8, 244.0], [88.9, 244.0], [89.0, 245.0], [89.1, 245.0], [89.2, 245.0], [89.3, 245.0], [89.4, 245.0], [89.5, 246.0], [89.6, 246.0], [89.7, 246.0], [89.8, 246.0], [89.9, 246.0], [90.0, 247.0], [90.1, 247.0], [90.2, 247.0], [90.3, 247.0], [90.4, 247.0], [90.5, 247.0], [90.6, 248.0], [90.7, 248.0], [90.8, 248.0], [90.9, 248.0], [91.0, 248.0], [91.1, 249.0], [91.2, 249.0], [91.3, 249.0], [91.4, 249.0], [91.5, 250.0], [91.6, 250.0], [91.7, 250.0], [91.8, 250.0], [91.9, 251.0], [92.0, 251.0], [92.1, 251.0], [92.2, 251.0], [92.3, 252.0], [92.4, 252.0], [92.5, 252.0], [92.6, 252.0], [92.7, 253.0], [92.8, 253.0], [92.9, 253.0], [93.0, 253.0], [93.1, 254.0], [93.2, 254.0], [93.3, 254.0], [93.4, 255.0], [93.5, 255.0], [93.6, 255.0], [93.7, 255.0], [93.8, 256.0], [93.9, 256.0], [94.0, 256.0], [94.1, 257.0], [94.2, 257.0], [94.3, 257.0], [94.4, 258.0], [94.5, 258.0], [94.6, 258.0], [94.7, 259.0], [94.8, 259.0], [94.9, 259.0], [95.0, 260.0], [95.1, 260.0], [95.2, 260.0], [95.3, 261.0], [95.4, 261.0], [95.5, 261.0], [95.6, 262.0], [95.7, 262.0], [95.8, 263.0], [95.9, 263.0], [96.0, 263.0], [96.1, 264.0], [96.2, 264.0], [96.3, 265.0], [96.4, 265.0], [96.5, 266.0], [96.6, 266.0], [96.7, 267.0], [96.8, 267.0], [96.9, 268.0], [97.0, 268.0], [97.1, 269.0], [97.2, 269.0], [97.3, 270.0], [97.4, 271.0], [97.5, 271.0], [97.6, 272.0], [97.7, 273.0], [97.8, 273.0], [97.9, 274.0], [98.0, 275.0], [98.1, 275.0], [98.2, 276.0], [98.3, 277.0], [98.4, 278.0], [98.5, 279.0], [98.6, 280.0], [98.7, 282.0], [98.8, 283.0], [98.9, 285.0], [99.0, 286.0], [99.1, 288.0], [99.2, 290.0], [99.3, 292.0], [99.4, 294.0], [99.5, 298.0], [99.6, 303.0], [99.7, 314.0], [99.8, 345.0], [99.9, 471.0]], "isOverall": false, "label": "HTTP Request", "isController": false}], "supportsControllersDiscrimination": true, "maxX": 100.0, "title": "Response Time Percentiles"}},
        getOptions: function() {
            return {
                series: {
                    points: { show: false }
                },
                legend: {
                    noColumns: 2,
                    show: true,
                    container: '#legendResponseTimePercentiles'
                },
                xaxis: {
                    tickDecimals: 1,
                    axisLabel: "Percentiles",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                yaxis: {
                    axisLabel: "Percentile value in ms",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20
                },
                grid: {
                    hoverable: true // IMPORTANT! this is needed for tooltip to
                                    // work
                },
                tooltip: true,
                tooltipOpts: {
                    content: "%s : %x.2 percentile was %y ms"
                },
                selection: { mode: "xy" },
            };
        },
        createGraph: function() {
            var data = this.data;
            var dataset = prepareData(data.result.series, $("#choicesResponseTimePercentiles"));
            var options = this.getOptions();
            prepareOptions(options, data);
            $.plot($("#flotResponseTimesPercentiles"), dataset, options);
            // setup overview
            $.plot($("#overviewResponseTimesPercentiles"), dataset, prepareOverviewOptions(options));
        }
};

/**
 * @param elementId Id of element where we display message
 */
function setEmptyGraph(elementId) {
    $(function() {
        $(elementId).text("No graph series with filter="+seriesFilter);
    });
}

// Response times percentiles
function refreshResponseTimePercentiles() {
    var infos = responseTimePercentilesInfos;
    prepareSeries(infos.data);
    if(infos.data.result.series.length == 0) {
        setEmptyGraph("#bodyResponseTimePercentiles");
        return;
    }
    if (isGraph($("#flotResponseTimesPercentiles"))){
        infos.createGraph();
    } else {
        var choiceContainer = $("#choicesResponseTimePercentiles");
        createLegend(choiceContainer, infos);
        infos.createGraph();
        setGraphZoomable("#flotResponseTimesPercentiles", "#overviewResponseTimesPercentiles");
        $('#bodyResponseTimePercentiles .legendColorBox > div').each(function(i){
            $(this).clone().prependTo(choiceContainer.find("li").eq(i));
        });
    }
}

var responseTimeDistributionInfos = {
        data: {"result": {"minY": 1.0, "minX": 100.0, "maxY": 25952.0, "series": [{"data": [[1100.0, 1.0], [300.0, 150.0], [600.0, 5.0], [1200.0, 5.0], [200.0, 25952.0], [400.0, 59.0], [100.0, 24807.0], [500.0, 21.0]], "isOverall": false, "label": "HTTP Request", "isController": false}], "supportsControllersDiscrimination": true, "granularity": 100, "maxX": 1200.0, "title": "Response Time Distribution"}},
        getOptions: function() {
            var granularity = this.data.result.granularity;
            return {
                legend: {
                    noColumns: 2,
                    show: true,
                    container: '#legendResponseTimeDistribution'
                },
                xaxis:{
                    axisLabel: "Response times in ms",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                yaxis: {
                    axisLabel: "Number of responses",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                bars : {
                    show: true,
                    barWidth: this.data.result.granularity
                },
                grid: {
                    hoverable: true // IMPORTANT! this is needed for tooltip to
                                    // work
                },
                tooltip: true,
                tooltipOpts: {
                    content: function(label, xval, yval, flotItem){
                        return yval + " responses for " + label + " were between " + xval + " and " + (xval + granularity) + " ms";
                    }
                }
            };
        },
        createGraph: function() {
            var data = this.data;
            var options = this.getOptions();
            prepareOptions(options, data);
            $.plot($("#flotResponseTimeDistribution"), prepareData(data.result.series, $("#choicesResponseTimeDistribution")), options);
        }

};

// Response time distribution
function refreshResponseTimeDistribution() {
    var infos = responseTimeDistributionInfos;
    prepareSeries(infos.data);
    if(infos.data.result.series.length == 0) {
        setEmptyGraph("#bodyResponseTimeDistribution");
        return;
    }
    if (isGraph($("#flotResponseTimeDistribution"))){
        infos.createGraph();
    }else{
        var choiceContainer = $("#choicesResponseTimeDistribution");
        createLegend(choiceContainer, infos);
        infos.createGraph();
        $('#footerResponseTimeDistribution .legendColorBox > div').each(function(i){
            $(this).clone().prependTo(choiceContainer.find("li").eq(i));
        });
    }
};


var syntheticResponseTimeDistributionInfos = {
        data: {"result": {"minY": 32.0, "minX": 0.0, "ticks": [[0, "Requests having \nresponse time <= 500ms"], [1, "Requests having \nresponse time > 500ms and <= 1 500ms"], [2, "Requests having \nresponse time > 1 500ms"], [3, "Requests in error"]], "maxY": 50968.0, "series": [{"data": [[0.0, 50968.0]], "color": "#9ACD32", "isOverall": false, "label": "Requests having \nresponse time <= 500ms", "isController": false}, {"data": [[1.0, 32.0]], "color": "yellow", "isOverall": false, "label": "Requests having \nresponse time > 500ms and <= 1 500ms", "isController": false}, {"data": [], "color": "orange", "isOverall": false, "label": "Requests having \nresponse time > 1 500ms", "isController": false}, {"data": [], "color": "#FF6347", "isOverall": false, "label": "Requests in error", "isController": false}], "supportsControllersDiscrimination": false, "maxX": 1.0, "title": "Synthetic Response Times Distribution"}},
        getOptions: function() {
            return {
                legend: {
                    noColumns: 2,
                    show: true,
                    container: '#legendSyntheticResponseTimeDistribution'
                },
                xaxis:{
                    axisLabel: "Response times ranges",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                    tickLength:0,
                    min:-0.5,
                    max:3.5
                },
                yaxis: {
                    axisLabel: "Number of responses",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                bars : {
                    show: true,
                    align: "center",
                    barWidth: 0.25,
                    fill:.75
                },
                grid: {
                    hoverable: true // IMPORTANT! this is needed for tooltip to
                                    // work
                },
                tooltip: true,
                tooltipOpts: {
                    content: function(label, xval, yval, flotItem){
                        return yval + " " + label;
                    }
                }
            };
        },
        createGraph: function() {
            var data = this.data;
            var options = this.getOptions();
            prepareOptions(options, data);
            options.xaxis.ticks = data.result.ticks;
            $.plot($("#flotSyntheticResponseTimeDistribution"), prepareData(data.result.series, $("#choicesSyntheticResponseTimeDistribution")), options);
        }

};

// Response time distribution
function refreshSyntheticResponseTimeDistribution() {
    var infos = syntheticResponseTimeDistributionInfos;
    prepareSeries(infos.data, true);
    if (isGraph($("#flotSyntheticResponseTimeDistribution"))){
        infos.createGraph();
    }else{
        var choiceContainer = $("#choicesSyntheticResponseTimeDistribution");
        createLegend(choiceContainer, infos);
        infos.createGraph();
        $('#footerSyntheticResponseTimeDistribution .legendColorBox > div').each(function(i){
            $(this).clone().prependTo(choiceContainer.find("li").eq(i));
        });
    }
};

var activeThreadsOverTimeInfos = {
        data: {"result": {"minY": 50.519932025773564, "minX": 1.7593443E12, "maxY": 51.0, "series": [{"data": [[1.75934442E12, 51.0], [1.7593443E12, 50.925475059382514], [1.75934448E12, 50.519932025773564], [1.75934436E12, 51.0]], "isOverall": false, "label": "Thread Group 1", "isController": false}], "supportsControllersDiscrimination": false, "granularity": 60000, "maxX": 1.75934448E12, "title": "Active Threads Over Time"}},
        getOptions: function() {
            return {
                series: {
                    stack: true,
                    lines: {
                        show: true,
                        fill: true
                    },
                    points: {
                        show: true
                    }
                },
                xaxis: {
                    mode: "time",
                    timeformat: getTimeFormat(this.data.result.granularity),
                    axisLabel: getElapsedTimeLabel(this.data.result.granularity),
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                yaxis: {
                    axisLabel: "Number of active threads",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20
                },
                legend: {
                    noColumns: 6,
                    show: true,
                    container: '#legendActiveThreadsOverTime'
                },
                grid: {
                    hoverable: true // IMPORTANT! this is needed for tooltip to
                                    // work
                },
                selection: {
                    mode: 'xy'
                },
                tooltip: true,
                tooltipOpts: {
                    content: "%s : At %x there were %y active threads"
                }
            };
        },
        createGraph: function() {
            var data = this.data;
            var dataset = prepareData(data.result.series, $("#choicesActiveThreadsOverTime"));
            var options = this.getOptions();
            prepareOptions(options, data);
            $.plot($("#flotActiveThreadsOverTime"), dataset, options);
            // setup overview
            $.plot($("#overviewActiveThreadsOverTime"), dataset, prepareOverviewOptions(options));
        }
};

// Active Threads Over Time
function refreshActiveThreadsOverTime(fixTimestamps) {
    var infos = activeThreadsOverTimeInfos;
    prepareSeries(infos.data);
    if(fixTimestamps) {
        fixTimeStamps(infos.data.result.series, 10800000);
    }
    if(isGraph($("#flotActiveThreadsOverTime"))) {
        infos.createGraph();
    }else{
        var choiceContainer = $("#choicesActiveThreadsOverTime");
        createLegend(choiceContainer, infos);
        infos.createGraph();
        setGraphZoomable("#flotActiveThreadsOverTime", "#overviewActiveThreadsOverTime");
        $('#footerActiveThreadsOverTime .legendColorBox > div').each(function(i){
            $(this).clone().prependTo(choiceContainer.find("li").eq(i));
        });
    }
};

var timeVsThreadsInfos = {
        data: {"result": {"minY": 157.0, "minX": 1.0, "maxY": 375.6666666666667, "series": [{"data": [[2.0, 188.8], [3.0, 209.66666666666666], [4.0, 194.6], [5.0, 157.0], [6.0, 186.0], [7.0, 195.75], [8.0, 202.33333333333331], [9.0, 238.6], [10.0, 218.0], [11.0, 211.5], [12.0, 211.5], [13.0, 215.5], [14.0, 189.0], [15.0, 194.0], [16.0, 227.0], [17.0, 211.0], [18.0, 190.0], [19.0, 201.25], [20.0, 193.0], [21.0, 204.66666666666666], [22.0, 161.66666666666666], [23.0, 208.14285714285714], [24.0, 215.66666666666666], [25.0, 223.125], [26.0, 217.86666666666667], [27.0, 208.66666666666666], [28.0, 174.5], [29.0, 229.0], [30.0, 218.0], [31.0, 193.33333333333331], [33.0, 180.33333333333334], [32.0, 201.55555555555554], [35.0, 204.4878048780488], [34.0, 226.0], [36.0, 327.3333333333333], [37.0, 255.6071428571428], [38.0, 375.6666666666667], [39.0, 363.75], [40.0, 322.5], [41.0, 191.66666666666666], [42.0, 214.82352941176467], [43.0, 199.35999999999999], [45.0, 183.0], [44.0, 212.04761904761904], [46.0, 198.19354838709677], [47.0, 201.5454545454546], [48.0, 205.43999999999997], [49.0, 209.23076923076925], [50.0, 222.0], [51.0, 203.03948905832232], [1.0, 209.0]], "isOverall": false, "label": "HTTP Request", "isController": false}, {"data": [[50.85721568627433, 203.1855294117657]], "isOverall": false, "label": "HTTP Request-Aggregated", "isController": false}], "supportsControllersDiscrimination": true, "maxX": 51.0, "title": "Time VS Threads"}},
        getOptions: function() {
            return {
                series: {
                    lines: {
                        show: true
                    },
                    points: {
                        show: true
                    }
                },
                xaxis: {
                    axisLabel: "Number of active threads",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                yaxis: {
                    axisLabel: "Average response times in ms",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20
                },
                legend: { noColumns: 2,show: true, container: '#legendTimeVsThreads' },
                selection: {
                    mode: 'xy'
                },
                grid: {
                    hoverable: true // IMPORTANT! this is needed for tooltip to work
                },
                tooltip: true,
                tooltipOpts: {
                    content: "%s: At %x.2 active threads, Average response time was %y.2 ms"
                }
            };
        },
        createGraph: function() {
            var data = this.data;
            var dataset = prepareData(data.result.series, $("#choicesTimeVsThreads"));
            var options = this.getOptions();
            prepareOptions(options, data);
            $.plot($("#flotTimesVsThreads"), dataset, options);
            // setup overview
            $.plot($("#overviewTimesVsThreads"), dataset, prepareOverviewOptions(options));
        }
};

// Time vs threads
function refreshTimeVsThreads(){
    var infos = timeVsThreadsInfos;
    prepareSeries(infos.data);
    if(infos.data.result.series.length == 0) {
        setEmptyGraph("#bodyTimeVsThreads");
        return;
    }
    if(isGraph($("#flotTimesVsThreads"))){
        infos.createGraph();
    }else{
        var choiceContainer = $("#choicesTimeVsThreads");
        createLegend(choiceContainer, infos);
        infos.createGraph();
        setGraphZoomable("#flotTimesVsThreads", "#overviewTimesVsThreads");
        $('#footerTimeVsThreads .legendColorBox > div').each(function(i){
            $(this).clone().prependTo(choiceContainer.find("li").eq(i));
        });
    }
};

var bytesThroughputOverTimeInfos = {
        data : {"result": {"minY": 32294.683333333334, "minX": 1.7593443E12, "maxY": 76433.91666666667, "series": [{"data": [[1.75934442E12, 72287.01666666666], [1.7593443E12, 32294.683333333334], [1.75934448E12, 67705.8], [1.75934436E12, 72212.5]], "isOverall": false, "label": "Bytes received per second", "isController": false}, {"data": [[1.75934442E12, 76433.91666666667], [1.7593443E12, 34142.61666666667], [1.75934448E12, 71583.76666666666], [1.75934436E12, 76339.7]], "isOverall": false, "label": "Bytes sent per second", "isController": false}], "supportsControllersDiscrimination": false, "granularity": 60000, "maxX": 1.75934448E12, "title": "Bytes Throughput Over Time"}},
        getOptions : function(){
            return {
                series: {
                    lines: {
                        show: true
                    },
                    points: {
                        show: true
                    }
                },
                xaxis: {
                    mode: "time",
                    timeformat: getTimeFormat(this.data.result.granularity),
                    axisLabel: getElapsedTimeLabel(this.data.result.granularity) ,
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                yaxis: {
                    axisLabel: "Bytes / sec",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                legend: {
                    noColumns: 2,
                    show: true,
                    container: '#legendBytesThroughputOverTime'
                },
                selection: {
                    mode: "xy"
                },
                grid: {
                    hoverable: true // IMPORTANT! this is needed for tooltip to
                                    // work
                },
                tooltip: true,
                tooltipOpts: {
                    content: "%s at %x was %y"
                }
            };
        },
        createGraph : function() {
            var data = this.data;
            var dataset = prepareData(data.result.series, $("#choicesBytesThroughputOverTime"));
            var options = this.getOptions();
            prepareOptions(options, data);
            $.plot($("#flotBytesThroughputOverTime"), dataset, options);
            // setup overview
            $.plot($("#overviewBytesThroughputOverTime"), dataset, prepareOverviewOptions(options));
        }
};

// Bytes throughput Over Time
function refreshBytesThroughputOverTime(fixTimestamps) {
    var infos = bytesThroughputOverTimeInfos;
    prepareSeries(infos.data);
    if(fixTimestamps) {
        fixTimeStamps(infos.data.result.series, 10800000);
    }
    if(isGraph($("#flotBytesThroughputOverTime"))){
        infos.createGraph();
    }else{
        var choiceContainer = $("#choicesBytesThroughputOverTime");
        createLegend(choiceContainer, infos);
        infos.createGraph();
        setGraphZoomable("#flotBytesThroughputOverTime", "#overviewBytesThroughputOverTime");
        $('#footerBytesThroughputOverTime .legendColorBox > div').each(function(i){
            $(this).clone().prependTo(choiceContainer.find("li").eq(i));
        });
    }
}

var responseTimesOverTimeInfos = {
        data: {"result": {"minY": 202.60464190981352, "minX": 1.7593443E12, "maxY": 204.2606888361043, "series": [{"data": [[1.75934442E12, 202.60464190981352], [1.7593443E12, 204.2606888361043], [1.75934448E12, 203.66161580400694], [1.75934436E12, 202.83985127149603]], "isOverall": false, "label": "HTTP Request", "isController": false}], "supportsControllersDiscrimination": true, "granularity": 60000, "maxX": 1.75934448E12, "title": "Response Time Over Time"}},
        getOptions: function(){
            return {
                series: {
                    lines: {
                        show: true
                    },
                    points: {
                        show: true
                    }
                },
                xaxis: {
                    mode: "time",
                    timeformat: getTimeFormat(this.data.result.granularity),
                    axisLabel: getElapsedTimeLabel(this.data.result.granularity),
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                yaxis: {
                    axisLabel: "Average response time in ms",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                legend: {
                    noColumns: 2,
                    show: true,
                    container: '#legendResponseTimesOverTime'
                },
                selection: {
                    mode: 'xy'
                },
                grid: {
                    hoverable: true // IMPORTANT! this is needed for tooltip to
                                    // work
                },
                tooltip: true,
                tooltipOpts: {
                    content: "%s : at %x Average response time was %y ms"
                }
            };
        },
        createGraph: function() {
            var data = this.data;
            var dataset = prepareData(data.result.series, $("#choicesResponseTimesOverTime"));
            var options = this.getOptions();
            prepareOptions(options, data);
            $.plot($("#flotResponseTimesOverTime"), dataset, options);
            // setup overview
            $.plot($("#overviewResponseTimesOverTime"), dataset, prepareOverviewOptions(options));
        }
};

// Response Times Over Time
function refreshResponseTimeOverTime(fixTimestamps) {
    var infos = responseTimesOverTimeInfos;
    prepareSeries(infos.data);
    if(infos.data.result.series.length == 0) {
        setEmptyGraph("#bodyResponseTimeOverTime");
        return;
    }
    if(fixTimestamps) {
        fixTimeStamps(infos.data.result.series, 10800000);
    }
    if(isGraph($("#flotResponseTimesOverTime"))){
        infos.createGraph();
    }else{
        var choiceContainer = $("#choicesResponseTimesOverTime");
        createLegend(choiceContainer, infos);
        infos.createGraph();
        setGraphZoomable("#flotResponseTimesOverTime", "#overviewResponseTimesOverTime");
        $('#footerResponseTimesOverTime .legendColorBox > div').each(function(i){
            $(this).clone().prependTo(choiceContainer.find("li").eq(i));
        });
    }
};

var latenciesOverTimeInfos = {
        data: {"result": {"minY": 202.5997347480109, "minX": 1.7593443E12, "maxY": 204.2448040380047, "series": [{"data": [[1.75934442E12, 202.5997347480109], [1.7593443E12, 204.2448040380047], [1.75934448E12, 203.6568717694536], [1.75934436E12, 202.8348051258222]], "isOverall": false, "label": "HTTP Request", "isController": false}], "supportsControllersDiscrimination": true, "granularity": 60000, "maxX": 1.75934448E12, "title": "Latencies Over Time"}},
        getOptions: function() {
            return {
                series: {
                    lines: {
                        show: true
                    },
                    points: {
                        show: true
                    }
                },
                xaxis: {
                    mode: "time",
                    timeformat: getTimeFormat(this.data.result.granularity),
                    axisLabel: getElapsedTimeLabel(this.data.result.granularity),
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                yaxis: {
                    axisLabel: "Average response latencies in ms",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                legend: {
                    noColumns: 2,
                    show: true,
                    container: '#legendLatenciesOverTime'
                },
                selection: {
                    mode: 'xy'
                },
                grid: {
                    hoverable: true // IMPORTANT! this is needed for tooltip to
                                    // work
                },
                tooltip: true,
                tooltipOpts: {
                    content: "%s : at %x Average latency was %y ms"
                }
            };
        },
        createGraph: function () {
            var data = this.data;
            var dataset = prepareData(data.result.series, $("#choicesLatenciesOverTime"));
            var options = this.getOptions();
            prepareOptions(options, data);
            $.plot($("#flotLatenciesOverTime"), dataset, options);
            // setup overview
            $.plot($("#overviewLatenciesOverTime"), dataset, prepareOverviewOptions(options));
        }
};

// Latencies Over Time
function refreshLatenciesOverTime(fixTimestamps) {
    var infos = latenciesOverTimeInfos;
    prepareSeries(infos.data);
    if(infos.data.result.series.length == 0) {
        setEmptyGraph("#bodyLatenciesOverTime");
        return;
    }
    if(fixTimestamps) {
        fixTimeStamps(infos.data.result.series, 10800000);
    }
    if(isGraph($("#flotLatenciesOverTime"))) {
        infos.createGraph();
    }else {
        var choiceContainer = $("#choicesLatenciesOverTime");
        createLegend(choiceContainer, infos);
        infos.createGraph();
        setGraphZoomable("#flotLatenciesOverTime", "#overviewLatenciesOverTime");
        $('#footerLatenciesOverTime .legendColorBox > div').each(function(i){
            $(this).clone().prependTo(choiceContainer.find("li").eq(i));
        });
    }
};

var connectTimeOverTimeInfos = {
        data: {"result": {"minY": 120.49857247194734, "minX": 1.7593443E12, "maxY": 122.04958432304036, "series": [{"data": [[1.75934442E12, 120.49986737400535], [1.7593443E12, 122.04958432304036], [1.75934448E12, 121.14324152092328], [1.75934436E12, 120.49857247194734]], "isOverall": false, "label": "HTTP Request", "isController": false}], "supportsControllersDiscrimination": true, "granularity": 60000, "maxX": 1.75934448E12, "title": "Connect Time Over Time"}},
        getOptions: function() {
            return {
                series: {
                    lines: {
                        show: true
                    },
                    points: {
                        show: true
                    }
                },
                xaxis: {
                    mode: "time",
                    timeformat: getTimeFormat(this.data.result.granularity),
                    axisLabel: getConnectTimeLabel(this.data.result.granularity),
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                yaxis: {
                    axisLabel: "Average Connect Time in ms",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                legend: {
                    noColumns: 2,
                    show: true,
                    container: '#legendConnectTimeOverTime'
                },
                selection: {
                    mode: 'xy'
                },
                grid: {
                    hoverable: true // IMPORTANT! this is needed for tooltip to
                                    // work
                },
                tooltip: true,
                tooltipOpts: {
                    content: "%s : at %x Average connect time was %y ms"
                }
            };
        },
        createGraph: function () {
            var data = this.data;
            var dataset = prepareData(data.result.series, $("#choicesConnectTimeOverTime"));
            var options = this.getOptions();
            prepareOptions(options, data);
            $.plot($("#flotConnectTimeOverTime"), dataset, options);
            // setup overview
            $.plot($("#overviewConnectTimeOverTime"), dataset, prepareOverviewOptions(options));
        }
};

// Connect Time Over Time
function refreshConnectTimeOverTime(fixTimestamps) {
    var infos = connectTimeOverTimeInfos;
    prepareSeries(infos.data);
    if(infos.data.result.series.length == 0) {
        setEmptyGraph("#bodyConnectTimeOverTime");
        return;
    }
    if(fixTimestamps) {
        fixTimeStamps(infos.data.result.series, 10800000);
    }
    if(isGraph($("#flotConnectTimeOverTime"))) {
        infos.createGraph();
    }else {
        var choiceContainer = $("#choicesConnectTimeOverTime");
        createLegend(choiceContainer, infos);
        infos.createGraph();
        setGraphZoomable("#flotConnectTimeOverTime", "#overviewConnectTimeOverTime");
        $('#footerConnectTimeOverTime .legendColorBox > div').each(function(i){
            $(this).clone().prependTo(choiceContainer.find("li").eq(i));
        });
    }
};

var responseTimePercentilesOverTimeInfos = {
        data: {"result": {"minY": 113.0, "minX": 1.7593443E12, "maxY": 1261.0, "series": [{"data": [[1.75934442E12, 1212.0], [1.7593443E12, 1193.0], [1.75934448E12, 1252.0], [1.75934436E12, 1261.0]], "isOverall": false, "label": "Max", "isController": false}, {"data": [[1.75934442E12, 245.0], [1.7593443E12, 247.0], [1.75934448E12, 248.0], [1.75934436E12, 246.0]], "isOverall": false, "label": "90th percentile", "isController": false}, {"data": [[1.75934442E12, 285.0], [1.7593443E12, 290.0], [1.75934448E12, 285.0], [1.75934436E12, 287.0]], "isOverall": false, "label": "99th percentile", "isController": false}, {"data": [[1.75934442E12, 259.0], [1.7593443E12, 261.0], [1.75934448E12, 260.0], [1.75934436E12, 260.0]], "isOverall": false, "label": "95th percentile", "isController": false}, {"data": [[1.75934442E12, 113.0], [1.7593443E12, 118.0], [1.75934448E12, 115.0], [1.75934436E12, 113.0]], "isOverall": false, "label": "Min", "isController": false}, {"data": [[1.75934442E12, 200.0], [1.7593443E12, 201.0], [1.75934448E12, 201.0], [1.75934436E12, 200.0]], "isOverall": false, "label": "Median", "isController": false}], "supportsControllersDiscrimination": false, "granularity": 60000, "maxX": 1.75934448E12, "title": "Response Time Percentiles Over Time (successful requests only)"}},
        getOptions: function() {
            return {
                series: {
                    lines: {
                        show: true,
                        fill: true
                    },
                    points: {
                        show: true
                    }
                },
                xaxis: {
                    mode: "time",
                    timeformat: getTimeFormat(this.data.result.granularity),
                    axisLabel: getElapsedTimeLabel(this.data.result.granularity),
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                yaxis: {
                    axisLabel: "Response Time in ms",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                legend: {
                    noColumns: 2,
                    show: true,
                    container: '#legendResponseTimePercentilesOverTime'
                },
                selection: {
                    mode: 'xy'
                },
                grid: {
                    hoverable: true // IMPORTANT! this is needed for tooltip to
                                    // work
                },
                tooltip: true,
                tooltipOpts: {
                    content: "%s : at %x Response time was %y ms"
                }
            };
        },
        createGraph: function () {
            var data = this.data;
            var dataset = prepareData(data.result.series, $("#choicesResponseTimePercentilesOverTime"));
            var options = this.getOptions();
            prepareOptions(options, data);
            $.plot($("#flotResponseTimePercentilesOverTime"), dataset, options);
            // setup overview
            $.plot($("#overviewResponseTimePercentilesOverTime"), dataset, prepareOverviewOptions(options));
        }
};

// Response Time Percentiles Over Time
function refreshResponseTimePercentilesOverTime(fixTimestamps) {
    var infos = responseTimePercentilesOverTimeInfos;
    prepareSeries(infos.data);
    if(fixTimestamps) {
        fixTimeStamps(infos.data.result.series, 10800000);
    }
    if(isGraph($("#flotResponseTimePercentilesOverTime"))) {
        infos.createGraph();
    }else {
        var choiceContainer = $("#choicesResponseTimePercentilesOverTime");
        createLegend(choiceContainer, infos);
        infos.createGraph();
        setGraphZoomable("#flotResponseTimePercentilesOverTime", "#overviewResponseTimePercentilesOverTime");
        $('#footerResponseTimePercentilesOverTime .legendColorBox > div').each(function(i){
            $(this).clone().prependTo(choiceContainer.find("li").eq(i));
        });
    }
};


var responseTimeVsRequestInfos = {
    data: {"result": {"minY": 187.0, "minX": 6.0, "maxY": 211.0, "series": [{"data": [[6.0, 187.0], [122.0, 202.5], [204.0, 202.0], [233.0, 211.0], [238.0, 205.0], [239.0, 202.5], [234.0, 205.0], [247.0, 202.0], [246.0, 200.5], [242.0, 200.5], [240.0, 205.0], [245.0, 203.5], [244.0, 203.0], [241.0, 204.0], [243.0, 203.0], [254.0, 201.0], [251.0, 201.0], [249.0, 201.0], [255.0, 199.0], [252.0, 200.0], [253.0, 200.0], [250.0, 201.0], [248.0, 202.0], [257.0, 197.0], [258.0, 198.0], [259.0, 197.0], [263.0, 196.0], [256.0, 199.0], [260.0, 201.0], [31.0, 197.0]], "isOverall": false, "label": "Successes", "isController": false}], "supportsControllersDiscrimination": false, "granularity": 1000, "maxX": 263.0, "title": "Response Time Vs Request"}},
    getOptions: function() {
        return {
            series: {
                lines: {
                    show: false
                },
                points: {
                    show: true
                }
            },
            xaxis: {
                axisLabel: "Global number of requests per second",
                axisLabelUseCanvas: true,
                axisLabelFontSizePixels: 12,
                axisLabelFontFamily: 'Verdana, Arial',
                axisLabelPadding: 20,
            },
            yaxis: {
                axisLabel: "Median Response Time in ms",
                axisLabelUseCanvas: true,
                axisLabelFontSizePixels: 12,
                axisLabelFontFamily: 'Verdana, Arial',
                axisLabelPadding: 20,
            },
            legend: {
                noColumns: 2,
                show: true,
                container: '#legendResponseTimeVsRequest'
            },
            selection: {
                mode: 'xy'
            },
            grid: {
                hoverable: true // IMPORTANT! this is needed for tooltip to work
            },
            tooltip: true,
            tooltipOpts: {
                content: "%s : Median response time at %x req/s was %y ms"
            },
            colors: ["#9ACD32", "#FF6347"]
        };
    },
    createGraph: function () {
        var data = this.data;
        var dataset = prepareData(data.result.series, $("#choicesResponseTimeVsRequest"));
        var options = this.getOptions();
        prepareOptions(options, data);
        $.plot($("#flotResponseTimeVsRequest"), dataset, options);
        // setup overview
        $.plot($("#overviewResponseTimeVsRequest"), dataset, prepareOverviewOptions(options));

    }
};

// Response Time vs Request
function refreshResponseTimeVsRequest() {
    var infos = responseTimeVsRequestInfos;
    prepareSeries(infos.data);
    if (isGraph($("#flotResponseTimeVsRequest"))){
        infos.createGraph();
    }else{
        var choiceContainer = $("#choicesResponseTimeVsRequest");
        createLegend(choiceContainer, infos);
        infos.createGraph();
        setGraphZoomable("#flotResponseTimeVsRequest", "#overviewResponseTimeVsRequest");
        $('#footerResponseRimeVsRequest .legendColorBox > div').each(function(i){
            $(this).clone().prependTo(choiceContainer.find("li").eq(i));
        });
    }
};


var latenciesVsRequestInfos = {
    data: {"result": {"minY": 187.0, "minX": 6.0, "maxY": 211.0, "series": [{"data": [[6.0, 187.0], [122.0, 202.5], [204.0, 202.0], [233.0, 211.0], [238.0, 205.0], [239.0, 202.5], [234.0, 205.0], [247.0, 202.0], [246.0, 200.5], [242.0, 200.5], [240.0, 205.0], [245.0, 203.5], [244.0, 203.0], [241.0, 204.0], [243.0, 203.0], [254.0, 201.0], [251.0, 201.0], [249.0, 201.0], [255.0, 199.0], [252.0, 200.0], [253.0, 200.0], [250.0, 201.0], [248.0, 202.0], [257.0, 197.0], [258.0, 198.0], [259.0, 197.0], [263.0, 196.0], [256.0, 199.0], [260.0, 201.0], [31.0, 197.0]], "isOverall": false, "label": "Successes", "isController": false}], "supportsControllersDiscrimination": false, "granularity": 1000, "maxX": 263.0, "title": "Latencies Vs Request"}},
    getOptions: function() {
        return{
            series: {
                lines: {
                    show: false
                },
                points: {
                    show: true
                }
            },
            xaxis: {
                axisLabel: "Global number of requests per second",
                axisLabelUseCanvas: true,
                axisLabelFontSizePixels: 12,
                axisLabelFontFamily: 'Verdana, Arial',
                axisLabelPadding: 20,
            },
            yaxis: {
                axisLabel: "Median Latency in ms",
                axisLabelUseCanvas: true,
                axisLabelFontSizePixels: 12,
                axisLabelFontFamily: 'Verdana, Arial',
                axisLabelPadding: 20,
            },
            legend: { noColumns: 2,show: true, container: '#legendLatencyVsRequest' },
            selection: {
                mode: 'xy'
            },
            grid: {
                hoverable: true // IMPORTANT! this is needed for tooltip to work
            },
            tooltip: true,
            tooltipOpts: {
                content: "%s : Median Latency time at %x req/s was %y ms"
            },
            colors: ["#9ACD32", "#FF6347"]
        };
    },
    createGraph: function () {
        var data = this.data;
        var dataset = prepareData(data.result.series, $("#choicesLatencyVsRequest"));
        var options = this.getOptions();
        prepareOptions(options, data);
        $.plot($("#flotLatenciesVsRequest"), dataset, options);
        // setup overview
        $.plot($("#overviewLatenciesVsRequest"), dataset, prepareOverviewOptions(options));
    }
};

// Latencies vs Request
function refreshLatenciesVsRequest() {
        var infos = latenciesVsRequestInfos;
        prepareSeries(infos.data);
        if(isGraph($("#flotLatenciesVsRequest"))){
            infos.createGraph();
        }else{
            var choiceContainer = $("#choicesLatencyVsRequest");
            createLegend(choiceContainer, infos);
            infos.createGraph();
            setGraphZoomable("#flotLatenciesVsRequest", "#overviewLatenciesVsRequest");
            $('#footerLatenciesVsRequest .legendColorBox > div').each(function(i){
                $(this).clone().prependTo(choiceContainer.find("li").eq(i));
            });
        }
};

var hitsPerSecondInfos = {
        data: {"result": {"minY": 113.11666666666666, "minX": 1.7593443E12, "maxY": 251.33333333333334, "series": [{"data": [[1.75934442E12, 251.33333333333334], [1.7593443E12, 113.11666666666666], [1.75934448E12, 234.53333333333333], [1.75934436E12, 251.01666666666668]], "isOverall": false, "label": "hitsPerSecond", "isController": false}], "supportsControllersDiscrimination": false, "granularity": 60000, "maxX": 1.75934448E12, "title": "Hits Per Second"}},
        getOptions: function() {
            return {
                series: {
                    lines: {
                        show: true
                    },
                    points: {
                        show: true
                    }
                },
                xaxis: {
                    mode: "time",
                    timeformat: getTimeFormat(this.data.result.granularity),
                    axisLabel: getElapsedTimeLabel(this.data.result.granularity),
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                yaxis: {
                    axisLabel: "Number of hits / sec",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20
                },
                legend: {
                    noColumns: 2,
                    show: true,
                    container: "#legendHitsPerSecond"
                },
                selection: {
                    mode : 'xy'
                },
                grid: {
                    hoverable: true // IMPORTANT! this is needed for tooltip to
                                    // work
                },
                tooltip: true,
                tooltipOpts: {
                    content: "%s at %x was %y.2 hits/sec"
                }
            };
        },
        createGraph: function createGraph() {
            var data = this.data;
            var dataset = prepareData(data.result.series, $("#choicesHitsPerSecond"));
            var options = this.getOptions();
            prepareOptions(options, data);
            $.plot($("#flotHitsPerSecond"), dataset, options);
            // setup overview
            $.plot($("#overviewHitsPerSecond"), dataset, prepareOverviewOptions(options));
        }
};

// Hits per second
function refreshHitsPerSecond(fixTimestamps) {
    var infos = hitsPerSecondInfos;
    prepareSeries(infos.data);
    if(fixTimestamps) {
        fixTimeStamps(infos.data.result.series, 10800000);
    }
    if (isGraph($("#flotHitsPerSecond"))){
        infos.createGraph();
    }else{
        var choiceContainer = $("#choicesHitsPerSecond");
        createLegend(choiceContainer, infos);
        infos.createGraph();
        setGraphZoomable("#flotHitsPerSecond", "#overviewHitsPerSecond");
        $('#footerHitsPerSecond .legendColorBox > div').each(function(i){
            $(this).clone().prependTo(choiceContainer.find("li").eq(i));
        });
    }
}

var codesPerSecondInfos = {
        data: {"result": {"minY": 112.26666666666667, "minX": 1.7593443E12, "maxY": 251.33333333333334, "series": [{"data": [[1.75934442E12, 251.33333333333334], [1.7593443E12, 112.26666666666667], [1.75934448E12, 235.38333333333333], [1.75934436E12, 251.01666666666668]], "isOverall": false, "label": "200", "isController": false}], "supportsControllersDiscrimination": false, "granularity": 60000, "maxX": 1.75934448E12, "title": "Codes Per Second"}},
        getOptions: function(){
            return {
                series: {
                    lines: {
                        show: true
                    },
                    points: {
                        show: true
                    }
                },
                xaxis: {
                    mode: "time",
                    timeformat: getTimeFormat(this.data.result.granularity),
                    axisLabel: getElapsedTimeLabel(this.data.result.granularity),
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                yaxis: {
                    axisLabel: "Number of responses / sec",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                legend: {
                    noColumns: 2,
                    show: true,
                    container: "#legendCodesPerSecond"
                },
                selection: {
                    mode: 'xy'
                },
                grid: {
                    hoverable: true // IMPORTANT! this is needed for tooltip to
                                    // work
                },
                tooltip: true,
                tooltipOpts: {
                    content: "Number of Response Codes %s at %x was %y.2 responses / sec"
                }
            };
        },
    createGraph: function() {
        var data = this.data;
        var dataset = prepareData(data.result.series, $("#choicesCodesPerSecond"));
        var options = this.getOptions();
        prepareOptions(options, data);
        $.plot($("#flotCodesPerSecond"), dataset, options);
        // setup overview
        $.plot($("#overviewCodesPerSecond"), dataset, prepareOverviewOptions(options));
    }
};

// Codes per second
function refreshCodesPerSecond(fixTimestamps) {
    var infos = codesPerSecondInfos;
    prepareSeries(infos.data);
    if(fixTimestamps) {
        fixTimeStamps(infos.data.result.series, 10800000);
    }
    if(isGraph($("#flotCodesPerSecond"))){
        infos.createGraph();
    }else{
        var choiceContainer = $("#choicesCodesPerSecond");
        createLegend(choiceContainer, infos);
        infos.createGraph();
        setGraphZoomable("#flotCodesPerSecond", "#overviewCodesPerSecond");
        $('#footerCodesPerSecond .legendColorBox > div').each(function(i){
            $(this).clone().prependTo(choiceContainer.find("li").eq(i));
        });
    }
};

var transactionsPerSecondInfos = {
        data: {"result": {"minY": 112.26666666666667, "minX": 1.7593443E12, "maxY": 251.33333333333334, "series": [{"data": [[1.75934442E12, 251.33333333333334], [1.7593443E12, 112.26666666666667], [1.75934448E12, 235.38333333333333], [1.75934436E12, 251.01666666666668]], "isOverall": false, "label": "HTTP Request-success", "isController": false}], "supportsControllersDiscrimination": true, "granularity": 60000, "maxX": 1.75934448E12, "title": "Transactions Per Second"}},
        getOptions: function(){
            return {
                series: {
                    lines: {
                        show: true
                    },
                    points: {
                        show: true
                    }
                },
                xaxis: {
                    mode: "time",
                    timeformat: getTimeFormat(this.data.result.granularity),
                    axisLabel: getElapsedTimeLabel(this.data.result.granularity),
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                yaxis: {
                    axisLabel: "Number of transactions / sec",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20
                },
                legend: {
                    noColumns: 2,
                    show: true,
                    container: "#legendTransactionsPerSecond"
                },
                selection: {
                    mode: 'xy'
                },
                grid: {
                    hoverable: true // IMPORTANT! this is needed for tooltip to
                                    // work
                },
                tooltip: true,
                tooltipOpts: {
                    content: "%s at %x was %y transactions / sec"
                }
            };
        },
    createGraph: function () {
        var data = this.data;
        var dataset = prepareData(data.result.series, $("#choicesTransactionsPerSecond"));
        var options = this.getOptions();
        prepareOptions(options, data);
        $.plot($("#flotTransactionsPerSecond"), dataset, options);
        // setup overview
        $.plot($("#overviewTransactionsPerSecond"), dataset, prepareOverviewOptions(options));
    }
};

// Transactions per second
function refreshTransactionsPerSecond(fixTimestamps) {
    var infos = transactionsPerSecondInfos;
    prepareSeries(infos.data);
    if(infos.data.result.series.length == 0) {
        setEmptyGraph("#bodyTransactionsPerSecond");
        return;
    }
    if(fixTimestamps) {
        fixTimeStamps(infos.data.result.series, 10800000);
    }
    if(isGraph($("#flotTransactionsPerSecond"))){
        infos.createGraph();
    }else{
        var choiceContainer = $("#choicesTransactionsPerSecond");
        createLegend(choiceContainer, infos);
        infos.createGraph();
        setGraphZoomable("#flotTransactionsPerSecond", "#overviewTransactionsPerSecond");
        $('#footerTransactionsPerSecond .legendColorBox > div').each(function(i){
            $(this).clone().prependTo(choiceContainer.find("li").eq(i));
        });
    }
};

var totalTPSInfos = {
        data: {"result": {"minY": 112.26666666666667, "minX": 1.7593443E12, "maxY": 251.33333333333334, "series": [{"data": [[1.75934442E12, 251.33333333333334], [1.7593443E12, 112.26666666666667], [1.75934448E12, 235.38333333333333], [1.75934436E12, 251.01666666666668]], "isOverall": false, "label": "Transaction-success", "isController": false}, {"data": [], "isOverall": false, "label": "Transaction-failure", "isController": false}], "supportsControllersDiscrimination": true, "granularity": 60000, "maxX": 1.75934448E12, "title": "Total Transactions Per Second"}},
        getOptions: function(){
            return {
                series: {
                    lines: {
                        show: true
                    },
                    points: {
                        show: true
                    }
                },
                xaxis: {
                    mode: "time",
                    timeformat: getTimeFormat(this.data.result.granularity),
                    axisLabel: getElapsedTimeLabel(this.data.result.granularity),
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                yaxis: {
                    axisLabel: "Number of transactions / sec",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20
                },
                legend: {
                    noColumns: 2,
                    show: true,
                    container: "#legendTotalTPS"
                },
                selection: {
                    mode: 'xy'
                },
                grid: {
                    hoverable: true // IMPORTANT! this is needed for tooltip to
                                    // work
                },
                tooltip: true,
                tooltipOpts: {
                    content: "%s at %x was %y transactions / sec"
                },
                colors: ["#9ACD32", "#FF6347"]
            };
        },
    createGraph: function () {
        var data = this.data;
        var dataset = prepareData(data.result.series, $("#choicesTotalTPS"));
        var options = this.getOptions();
        prepareOptions(options, data);
        $.plot($("#flotTotalTPS"), dataset, options);
        // setup overview
        $.plot($("#overviewTotalTPS"), dataset, prepareOverviewOptions(options));
    }
};

// Total Transactions per second
function refreshTotalTPS(fixTimestamps) {
    var infos = totalTPSInfos;
    // We want to ignore seriesFilter
    prepareSeries(infos.data, false, true);
    if(fixTimestamps) {
        fixTimeStamps(infos.data.result.series, 10800000);
    }
    if(isGraph($("#flotTotalTPS"))){
        infos.createGraph();
    }else{
        var choiceContainer = $("#choicesTotalTPS");
        createLegend(choiceContainer, infos);
        infos.createGraph();
        setGraphZoomable("#flotTotalTPS", "#overviewTotalTPS");
        $('#footerTotalTPS .legendColorBox > div').each(function(i){
            $(this).clone().prependTo(choiceContainer.find("li").eq(i));
        });
    }
};

// Collapse the graph matching the specified DOM element depending the collapsed
// status
function collapse(elem, collapsed){
    if(collapsed){
        $(elem).parent().find(".fa-chevron-up").removeClass("fa-chevron-up").addClass("fa-chevron-down");
    } else {
        $(elem).parent().find(".fa-chevron-down").removeClass("fa-chevron-down").addClass("fa-chevron-up");
        if (elem.id == "bodyBytesThroughputOverTime") {
            if (isGraph($(elem).find('.flot-chart-content')) == false) {
                refreshBytesThroughputOverTime(true);
            }
            document.location.href="#bytesThroughputOverTime";
        } else if (elem.id == "bodyLatenciesOverTime") {
            if (isGraph($(elem).find('.flot-chart-content')) == false) {
                refreshLatenciesOverTime(true);
            }
            document.location.href="#latenciesOverTime";
        } else if (elem.id == "bodyCustomGraph") {
            if (isGraph($(elem).find('.flot-chart-content')) == false) {
                refreshCustomGraph(true);
            }
            document.location.href="#responseCustomGraph";
        } else if (elem.id == "bodyConnectTimeOverTime") {
            if (isGraph($(elem).find('.flot-chart-content')) == false) {
                refreshConnectTimeOverTime(true);
            }
            document.location.href="#connectTimeOverTime";
        } else if (elem.id == "bodyResponseTimePercentilesOverTime") {
            if (isGraph($(elem).find('.flot-chart-content')) == false) {
                refreshResponseTimePercentilesOverTime(true);
            }
            document.location.href="#responseTimePercentilesOverTime";
        } else if (elem.id == "bodyResponseTimeDistribution") {
            if (isGraph($(elem).find('.flot-chart-content')) == false) {
                refreshResponseTimeDistribution();
            }
            document.location.href="#responseTimeDistribution" ;
        } else if (elem.id == "bodySyntheticResponseTimeDistribution") {
            if (isGraph($(elem).find('.flot-chart-content')) == false) {
                refreshSyntheticResponseTimeDistribution();
            }
            document.location.href="#syntheticResponseTimeDistribution" ;
        } else if (elem.id == "bodyActiveThreadsOverTime") {
            if (isGraph($(elem).find('.flot-chart-content')) == false) {
                refreshActiveThreadsOverTime(true);
            }
            document.location.href="#activeThreadsOverTime";
        } else if (elem.id == "bodyTimeVsThreads") {
            if (isGraph($(elem).find('.flot-chart-content')) == false) {
                refreshTimeVsThreads();
            }
            document.location.href="#timeVsThreads" ;
        } else if (elem.id == "bodyCodesPerSecond") {
            if (isGraph($(elem).find('.flot-chart-content')) == false) {
                refreshCodesPerSecond(true);
            }
            document.location.href="#codesPerSecond";
        } else if (elem.id == "bodyTransactionsPerSecond") {
            if (isGraph($(elem).find('.flot-chart-content')) == false) {
                refreshTransactionsPerSecond(true);
            }
            document.location.href="#transactionsPerSecond";
        } else if (elem.id == "bodyTotalTPS") {
            if (isGraph($(elem).find('.flot-chart-content')) == false) {
                refreshTotalTPS(true);
            }
            document.location.href="#totalTPS";
        } else if (elem.id == "bodyResponseTimeVsRequest") {
            if (isGraph($(elem).find('.flot-chart-content')) == false) {
                refreshResponseTimeVsRequest();
            }
            document.location.href="#responseTimeVsRequest";
        } else if (elem.id == "bodyLatenciesVsRequest") {
            if (isGraph($(elem).find('.flot-chart-content')) == false) {
                refreshLatenciesVsRequest();
            }
            document.location.href="#latencyVsRequest";
        }
    }
}

/*
 * Activates or deactivates all series of the specified graph (represented by id parameter)
 * depending on checked argument.
 */
function toggleAll(id, checked){
    var placeholder = document.getElementById(id);

    var cases = $(placeholder).find(':checkbox');
    cases.prop('checked', checked);
    $(cases).parent().children().children().toggleClass("legend-disabled", !checked);

    var choiceContainer;
    if ( id == "choicesBytesThroughputOverTime"){
        choiceContainer = $("#choicesBytesThroughputOverTime");
        refreshBytesThroughputOverTime(false);
    } else if(id == "choicesResponseTimesOverTime"){
        choiceContainer = $("#choicesResponseTimesOverTime");
        refreshResponseTimeOverTime(false);
    }else if(id == "choicesResponseCustomGraph"){
        choiceContainer = $("#choicesResponseCustomGraph");
        refreshCustomGraph(false);
    } else if ( id == "choicesLatenciesOverTime"){
        choiceContainer = $("#choicesLatenciesOverTime");
        refreshLatenciesOverTime(false);
    } else if ( id == "choicesConnectTimeOverTime"){
        choiceContainer = $("#choicesConnectTimeOverTime");
        refreshConnectTimeOverTime(false);
    } else if ( id == "choicesResponseTimePercentilesOverTime"){
        choiceContainer = $("#choicesResponseTimePercentilesOverTime");
        refreshResponseTimePercentilesOverTime(false);
    } else if ( id == "choicesResponseTimePercentiles"){
        choiceContainer = $("#choicesResponseTimePercentiles");
        refreshResponseTimePercentiles();
    } else if(id == "choicesActiveThreadsOverTime"){
        choiceContainer = $("#choicesActiveThreadsOverTime");
        refreshActiveThreadsOverTime(false);
    } else if ( id == "choicesTimeVsThreads"){
        choiceContainer = $("#choicesTimeVsThreads");
        refreshTimeVsThreads();
    } else if ( id == "choicesSyntheticResponseTimeDistribution"){
        choiceContainer = $("#choicesSyntheticResponseTimeDistribution");
        refreshSyntheticResponseTimeDistribution();
    } else if ( id == "choicesResponseTimeDistribution"){
        choiceContainer = $("#choicesResponseTimeDistribution");
        refreshResponseTimeDistribution();
    } else if ( id == "choicesHitsPerSecond"){
        choiceContainer = $("#choicesHitsPerSecond");
        refreshHitsPerSecond(false);
    } else if(id == "choicesCodesPerSecond"){
        choiceContainer = $("#choicesCodesPerSecond");
        refreshCodesPerSecond(false);
    } else if ( id == "choicesTransactionsPerSecond"){
        choiceContainer = $("#choicesTransactionsPerSecond");
        refreshTransactionsPerSecond(false);
    } else if ( id == "choicesTotalTPS"){
        choiceContainer = $("#choicesTotalTPS");
        refreshTotalTPS(false);
    } else if ( id == "choicesResponseTimeVsRequest"){
        choiceContainer = $("#choicesResponseTimeVsRequest");
        refreshResponseTimeVsRequest();
    } else if ( id == "choicesLatencyVsRequest"){
        choiceContainer = $("#choicesLatencyVsRequest");
        refreshLatenciesVsRequest();
    }
    var color = checked ? "black" : "#818181";
    if(choiceContainer != null) {
        choiceContainer.find("label").each(function(){
            this.style.color = color;
        });
    }
}

