// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            NBTTagCompound

public class ServerNBTStorage
{

    public String field_35795_a;
    public String field_35793_b;
    public String field_35794_c;
    public String field_35791_d;
    public long field_35792_e;
    public boolean field_35790_f;

    public ServerNBTStorage(String s, String s1)
    {
        field_35790_f = false;
        field_35795_a = s;
        field_35793_b = s1;
    }

    public NBTTagCompound func_35789_a()
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        nbttagcompound.func_754_a("name", field_35795_a);
        nbttagcompound.func_754_a("ip", field_35793_b);
        return nbttagcompound;
    }

    public static ServerNBTStorage func_35788_a(NBTTagCompound nbttagcompound)
    {
        return new ServerNBTStorage(nbttagcompound.func_755_i("name"), nbttagcompound.func_755_i("ip"));
    }
}
