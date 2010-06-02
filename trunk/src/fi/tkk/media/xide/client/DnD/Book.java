/**
 * 
 */
package fi.tkk.media.xide.client.DnD;
/*
 * Copyright 2008 Chris Fong
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */


import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.SourcesMouseEvents;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Book extends Composite implements SourcesMouseEvents {

        private String title;
        private String imgUrl;
        private int price;
        private Image bookImage;
        private VerticalPanel mainPanel;
        
        public Book(String title, int i, String imgUrl)
        {
                this.title = title;
                this.price = i;
                this.imgUrl = imgUrl;
                this.mainPanel = new VerticalPanel();
                initWidget(mainPanel);
                
                bookImage = new Image(imgUrl);
                mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
                mainPanel.add(bookImage);
                mainPanel.add(new Label(title));
                mainPanel.add(new Label("$"+i));
                mainPanel.addStyleName("book");
        }

        public void addMouseListener(MouseListener listener) {
                bookImage.addMouseListener(listener);
                
        }

        public void removeMouseListener(MouseListener listener) {
                bookImage.removeMouseListener(listener);
        }
        
        public String getBookTitle() {
                return this.title;
        }

        public Image getImage() {
                return this.bookImage;
        }
        
        public String getImageUrl() {
                return this.imgUrl;
        }

        public int getPrice() {
                return price;
        }

        public int hashCode() {
                final int prime = 31;
                int result = 1;
                result = prime * result + ((title == null) ? 0 : title.hashCode());
                return result;
        }

        public boolean equals(Object obj) {
                if (this == obj)
                        return true;
                if (obj == null)
                        return false;
                Book other = (Book) obj;
                if (title == null) {
                        if (other.title != null)
                                return false;
                } else if (!title.equals(other.title))
                        return false;
                return true;
        }
        
        
}
