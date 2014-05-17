package com.danielbchapman.utility;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.Window;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * A group of static helper methods that provide basic UI functionality like 
 * centering windows, laying out panels and providing quick, easy access to 
 * GridBagConstraints that have a large number of properties set.
 *
 ***************************************************************************
 * @author Daniel B. Chapman 
 * <br /><i><b>Light Assistant</b></i> copyright Daniel B. Chapman
 * @since May 3, 2012
 * @version 2 Development
 * @link http://www.lightassistant.com
 ***************************************************************************
 */
public class UiUtility
{
  
  public static Color clone(Color one)
  {
    return new Color(one.getRed(), one.getGreen(), one.getBlue(), one.getAlpha());
  }

  public static Color cloneSetOpacity(Color one, int opacity)
  {
    return new Color(one.getRed(), one.getGreen(), one.getBlue(), opacity);
  }
  
  public static GridBagConstraints getNoFill(int x, int y)
  {
    GridBagConstraints gbc = new GridBagConstraints();
    return setCoordinates(gbc, x, y);
  }
  
  public static void centerFrame(Window window)
  {
    // Get the default toolkit
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    Dimension parent = toolkit.getScreenSize();

    int x = Double.valueOf((parent.getWidth() - window.getWidth()) / 2).intValue();
    int y = Double.valueOf((parent.getHeight() - window.getHeight()) / 2).intValue();

    window.setLocation(x, y);
  }

  public static void centerFrame(Window parent, Window child)
  {
    int x = (parent.getWidth() - child.getWidth()) / 2;
    int y = (parent.getHeight() - child.getHeight()) / 2;
    child.setLocation(parent.getLocation().x + x, parent.getLocation().y + y);
  }

  /**
   * This method is decoupled so the library
   * can be used without SWT. It is a utility method
   * for centering a shell so the Reflection overhead is
   * probably fine.
   * @param shell the org.eclipse.swt.widgets.Shell to center  
   * @throws ClassNotFoundException 
   * 
   */
  public static void centerShell(Object shell)
  {
    try
    {
      Class<?> shellClass = Class.forName("org.eclipse.swt.widgets.Shell");

      if (shell.getClass().isAssignableFrom(shellClass))
      {
        Class<?> rectangleClass = Class.forName("org.eclipse.swt.graphics.Rectangle");
        Method getMonitor = shellClass.getMethod("getMonitor", new Class[] {});
        Method setLocation = shellClass.getMethod("setLocation", new Class[] { int.class, int.class });
        Method getSize = shellClass.getMethod("getSize", new Class[] {});

        Field rectWidth = rectangleClass.getField("width");
        Field rectHeight = rectangleClass.getField("height");
        Object shellRect = getSize.invoke(shell, new Object[] {});

        Object monitor = getMonitor.invoke(shell, new Object[] {});
        Method getBounds = monitor.getClass().getMethod("getBounds", new Class[] {});
        Field boundsX = monitor.getClass().getField("x");
        Field boundsY = monitor.getClass().getField("y");
        Object bounds = getBounds.invoke(monitor, new Object[] {});

        int x = boundsX.getInt(bounds);
        int y = boundsY.getInt(bounds);
        int width = rectWidth.getInt(shellRect);
        int height = rectHeight.getInt(shellRect);

        setLocation.invoke(shell, width - x, height - y);
      }
      else
        throw new IllegalArgumentException("Class is not assignable from org.eclipse.swt.widgets.Shell");
    }
    catch (ClassNotFoundException e)
    {
      throw new RuntimeException(e.getMessage(), e);
    }
    catch (NoSuchMethodException e)
    {
      throw new RuntimeException(e.getMessage(), e);
    }
    catch (SecurityException e)
    {
      throw new RuntimeException(e.getMessage(), e);
    }
    catch (NoSuchFieldException e)
    {
      throw new RuntimeException(e.getMessage(), e);
    }
    catch (IllegalArgumentException e)
    {
      throw new RuntimeException(e.getMessage(), e);
    }
    catch (IllegalAccessException e)
    {
      throw new RuntimeException(e.getMessage(), e);
    }
    catch (InvocationTargetException e)
    {
      throw new RuntimeException(e.getMessage(), e);
    }

    /* Method refactored from below */
    // org.eclipse.swt.graphics.Rectangle rect = shell.getMonitor().getBounds();
    // shell.setLocation(rect.width - shell.getSize().x, rect.height - shell.getSize().y);
  }

  public static GridBagConstraints getFillBoth()
  {
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.LINE_START;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.weightx = 1.0;
    gbc.weighty = 1.0;

    return gbc;
  }

  public static GridBagConstraints getFillBoth(int x, int y)
  {
    return setCoordinates(getFillBoth(), x, y);
  }

  public static GridBagConstraints getFillHorizontal()
  {
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.LINE_START;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1.0;
    gbc.weighty = 0.0;

    return gbc;
  }

  public static GridBagConstraints getFillHorizontal(int x, int y)
  {
    return setCoordinates(getFillHorizontal(), x, y);
  }

  public static GridBagConstraints getFillVertical()
  {
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.LINE_START;
    gbc.fill = GridBagConstraints.VERTICAL;
    gbc.weightx = 0.0;
    gbc.weighty = 1.0;

    return gbc;
  }

  public static GridBagConstraints getFillVertical(int x, int y)
  {
    return setCoordinates(getFillVertical(), x, y);
  }

  public static GridBagConstraints getStandardTitleConstraints(int x, int y)
  {
    GridBagConstraints ret = getFillHorizontal(x, y);
    ret.insets.top = 5;
    ret.insets.bottom = 5;
    return ret;
  }

  /**
   * Set this component layout to be a GridBagLayout
   * @param comp <Return Description>  
   * 
   */
  public static void gridBag(JComponent comp)
  {
    comp.setLayout(new GridBagLayout());
  }

  /**
   * Layout this panel so it fits the "Standard Panel" look of the application.
   * @param parent the parent
   * @param child the child panel  
   * 
   */
  public static void layoutStandardPanel(JPanel parent, JPanel child)
  {
    parent.removeAll();
    parent.setLayout(new GridBagLayout());

    GridBagConstraints container = new GridBagConstraints();

    container.anchor = GridBagConstraints.FIRST_LINE_START;
    container.fill = GridBagConstraints.BOTH;
    container.weightx = 1.0;
    container.weighty = 1.0;
    container.insets.bottom = 15;
    container.insets.left = 15;
    container.insets.right = 15;
    container.insets.top = 5;
    container.gridy = 0;
    container.gridx = 0;

    parent.add(child, container);
  }

  public static GridBagConstraints setCoordinates(GridBagConstraints gbc, int x, int y)
  {
    gbc.gridx = x;
    gbc.gridy = y;
    return gbc;
  }
  
  public static JFrame testFrame(JComponent comp)
  {
    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(400, 300);

    frame.setLayout(new GridBagLayout());
    if (comp != null)
      frame.add(comp, UiUtility.getFillBoth());

    frame.setLocation(200, 200);
    return frame;
  }
}
