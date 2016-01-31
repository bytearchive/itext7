package com.itextpdf.layout.element;

import com.itextpdf.kernel.pdf.tagutils.IAccessibleElement;
import com.itextpdf.layout.Property;
import com.itextpdf.layout.renderer.BlockRenderer;

public abstract class BlockElement<T extends BlockElement> extends AbstractElement<T> implements IElement<T>, IAccessibleElement {

    public BlockElement() {
    }

    public Float getMarginLeft() {
        return getProperty(Property.MARGIN_LEFT);
    }

    public T setMarginLeft(float value) {
        return setProperty(Property.MARGIN_LEFT, value);
    }

    public Float getMarginRight() {
        return getProperty(Property.MARGIN_RIGHT);
    }

    public T setMarginRight(float value) {
        return setProperty(Property.MARGIN_RIGHT, value);
    }

    public Float getMarginTop() {
        return getProperty(Property.MARGIN_TOP);
    }

    public T setMarginTop(float value) {
        return setProperty(Property.MARGIN_TOP, value);
    }

    public Float getMarginBottom() {
        return getProperty(Property.MARGIN_BOTTOM);
    }

    public T setMarginBottom(float value) {
        return setProperty(Property.MARGIN_BOTTOM, value);
    }

    public T setMargin(float commonMargin) {
        return setMargins(commonMargin, commonMargin, commonMargin, commonMargin);
    }

    public T setMargins(float marginTop, float marginRight, float marginBottom, float marginLeft) {
        return (T) setMarginTop(marginTop).setMarginRight(marginRight).setMarginBottom(marginBottom).setMarginLeft(marginLeft);
    }

    public Float getPaddingLeft() {
        return getProperty(Property.PADDING_LEFT);
    }

    public T setPaddingLeft(float value) {
        return setProperty(Property.PADDING_LEFT, value);
    }

    public Float getPaddingRight() {
        return getProperty(Property.PADDING_RIGHT);
    }

    public T setPaddingRight(float value) {
        return setProperty(Property.PADDING_RIGHT, value);
    }

    public Float getPaddingTop() {
        return getProperty(Property.PADDING_TOP);
    }

    public T setPaddingTop(float value) {
        return setProperty(Property.PADDING_TOP, value);
    }

    public Float getPaddingBottom() {
        return getProperty(Property.PADDING_BOTTOM);
    }

    public T setPaddingBottom(float value) {
        return setProperty(Property.PADDING_BOTTOM, value);
    }

    public T setPadding(float commonPadding) {
        return setPaddings(commonPadding, commonPadding, commonPadding, commonPadding);
    }

    public T setPaddings(float paddingTop, float paddingRight, float paddingBottom, float paddingLeft) {
        return (T) setPaddingTop(paddingTop).setPaddingRight(paddingRight).setPaddingBottom(paddingBottom).setPaddingLeft(paddingLeft);
    }

    public T setVerticalAlignment(Property.VerticalAlignment verticalAlignment) {
        return setProperty(Property.VERTICAL_ALIGNMENT, verticalAlignment);
    }

    /**
     * Sets a ratio which determines in which proportion will word spacing and character spacing
     * be applied when horizontal alignment is justified.
     * @param ratio the ratio coefficient. It must be between 0 and 1, inclusive.
     *              It means that <b>ratio</b> part of the free space will
     *              be compensated by word spacing, and <b>1-ratio</b> part of the free space will
     *              be compensated by character spacing.
     *              If <b>ratio</b> is 1, additional character spacing will not be applied.
     *              If <b>ratio</b> is 0, additional word spacing will not be applied.
     */
    public T setSpacingRatio(float ratio) {
        return setProperty(Property.SPACING_RATIO, ratio);
    }

    @Override
    public <T> T getDefaultProperty(Property property) {
        switch (property) {
            case KEEP_TOGETHER:
                return (T) Boolean.valueOf(false);
            default:
                return super.getDefaultProperty(property);
        }
    }

    public Boolean isKeepTogether() {
        return getProperty(Property.KEEP_TOGETHER);
    }

    public T setKeepTogether(boolean keepTogether) {
        return setProperty(Property.KEEP_TOGETHER, keepTogether);
    }

    public T setRotationAngle(float angle) {
        setProperty(Property.ROTATION_ANGLE, angle);
        return (T) this;
    }

    public T setRotationAngle(double angle) {
        setProperty(Property.ROTATION_ANGLE, Float.valueOf((float) angle));
        return (T) this;
    }

    @Override
    protected BlockRenderer makeNewRenderer() {
        return new BlockRenderer(this);
    }
}