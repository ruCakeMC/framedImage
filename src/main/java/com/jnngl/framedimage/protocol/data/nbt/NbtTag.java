/*
 *  Copyright (C) 2022  JNNGL
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.jnngl.framedimage.protocol.data.nbt;

import io.netty.buffer.ByteBuf;

public abstract class NbtTag {

  private String name;

  public NbtTag(String name) {
    this.name = name;
  }

  public abstract int getTypeID();
  public abstract void encode(ByteBuf buf);

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
